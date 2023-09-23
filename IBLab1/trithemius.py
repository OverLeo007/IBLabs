import hashlib

eng_alph = "abcdefghijklmnopqrstuvwxyz"
rus_alph = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя"
nums_syms = "1234567890 .,"


class Trithemius:
    def __init__(self, alph, key):
        self.k_coeffs = None
        self.key_hash = None
        self.iterations = None
        self.alph = alph
        self.a_len = len(self.alph)
        self.upd_key(key)
        
    def upd_key(self, key):
        hashed_key = hashlib.md5(key.encode('utf-8')).hexdigest()
        hexed = [hashed_key[i:i + 4] for i in range(0, len(hashed_key), 4)]
        self.key_hash = hashed_key
        for x, val in enumerate(hexed):
            hexed[x] = int('-1' if int(val[:2], 16) % 2 == 1 else '1') * int(val[2:],
                                                                             16)
        self.iterations = abs(hexed[0])
        self.k_coeffs = hexed[1:]

    @staticmethod
    def circular_shift(coeffs, is_left=False):
        n = len(coeffs)
        i = 0
        while True:
            shifted = [
                coeffs[(j - (-1) ** is_left * i) % n] for j in range(n)
            ]
            yield shifted
            i += 1

    def circular_shift_by_iter(self, iteration):
        n = len(self.k_coeffs)
        shifted = [self.k_coeffs[(j - iteration) % n] for j in range(n)]
        return shifted

    @staticmethod
    def solve_polynomial(x_value, coeffs):
        res = 0
        for coeff, power in zip(coeffs, reversed(range(len(coeffs)))):
            res += coeff * (x_value ** power)
        return res

    def gronfeld(self, text, is_decrypt=False):
        key = [int(x, 16) for x in self.key_hash]
        while len(key) < len(text):
            key.extend(key)
        key = key[:len(text)]

        return "".join([
            self.alph[
                (
                        self.alph.index(char) +
                        (-1) ** is_decrypt * (k_val % self.a_len)
                ) % self.a_len
                ]
            if char in self.alph else char
            for k_val, char in zip(key, text)
        ])

    def permute(self, text, is_decrypt=False):
        text_by_8 = [list(text[i: i + 8]) for i in range(0, len(text), 8)]
        by_8 = [bool(int(self.key_hash[i:i + 4], 16) % 2) for i in
                range(0, len(self.key_hash), 4)]

        for eight_gramm in text_by_8:
            cropped_key = by_8[:len(eight_gramm)]
            for_iterable = range(len(cropped_key) - 1, -1, -1) \
                if is_decrypt else range(len(cropped_key))
            for i in for_iterable:
                if cropped_key[i]:
                    next_idx = (i + 1) % len(eight_gramm)
                    eight_gramm[i], eight_gramm[next_idx] = \
                        eight_gramm[next_idx], eight_gramm[i]

        return "".join([
            "".join([char for char in eight_gramm]) for eight_gramm in text_by_8
        ])

    def encrypt(self, text):
        coeffs_iterator = self.circular_shift(self.k_coeffs)
        cur_text = text
        for i in range(self.iterations):
            cur_coeffs = next(coeffs_iterator)
            cur_text = ''.join([
                self.alph[
                    (
                            self.alph.index(sym) +
                            self.solve_polynomial(i, cur_coeffs)
                    ) % self.a_len
                    ]
                if sym in self.alph else sym
                for i, sym in enumerate(cur_text.lower())
            ])

        substituted_text = self.gronfeld(cur_text, is_decrypt=False)
        permuted_text = self.permute(substituted_text, is_decrypt=False)

        return permuted_text

    def decrypt(self, text):

        unpermuted_text = self.permute(text, is_decrypt=True)
        unsubstituted_text = self.gronfeld(unpermuted_text, is_decrypt=True)
        cur_text = unsubstituted_text

        coeffs_iterator = self.circular_shift(
            self.circular_shift_by_iter(self.iterations - 1), is_left=True
        )
        for i in range(self.iterations):
            cur_coeffs = next(coeffs_iterator)
            cur_text = ''.join([
                self.alph[
                    (
                            self.alph.index(sym) -
                            self.solve_polynomial(i, cur_coeffs)
                    ) % self.a_len
                    ]
                if sym in self.alph else sym
                for i, sym in enumerate(cur_text.lower())
            ])

        return cur_text

    def create_polynomial_str(self):
        n = len(self.k_coeffs)
        if n == 0 or self.k_coeffs[0] == 0:
            return "0"

        polynomial = ""
        for i in range(n):
            coef = self.k_coeffs[i]
            power = n - i - 1

            if coef != 0:
                if power > 1:
                    if coef == 1:
                        polynomial += f"+x^{power}"
                    elif coef == -1:
                        polynomial += f"-x^{power}"
                    else:
                        polynomial += f"{'+' if coef > 0 else ''}{coef}x^{power}"
                elif power == 1:
                    polynomial += f"{'+' if coef > 0 else ''}{coef}x"
                else:
                    polynomial += f"{'+' if coef > 0 else ''}{coef}"

        polynomial = polynomial[1:]

        return polynomial

    def __str__(self):
        return f"k formula: {self.create_polynomial_str()}\n" \
               f"Iterations count: {self.iterations}"


if __name__ == '__main__':
    alph = eng_alph + rus_alph + nums_syms
    trit = Trithemius(alph, "amobus123a+")
    o_text = "Съешь же ещё этих мягких французских булок, да выпей чаю."
    c_txt = trit.encrypt(o_text)
    op_text = trit.decrypt(c_txt)
    print(o_text)
    print(c_txt)
    print(op_text)
    print(trit)
