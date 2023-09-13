eng_alph = "abcdefghijklmnopqrstuvwxyz"
rus_alph = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя"
nums_syms = "1234567890 .,"


class Trithemius:
    def __init__(self, alph, *k_coeffs, iterations=1):
        self.alph = alph
        self.a_len = len(self.alph)
        self.iterations = iterations
        self.k_coeffs = k_coeffs

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
        return cur_text

    def decrypt(self, text):
        coeffs_iterator = self.circular_shift(
            self.circular_shift_by_iter(self.iterations - 1), is_left=True
        )
        cur_text = text
        for i in range(self.iterations):
            cur_coeffs = next(coeffs_iterator)
            cur_text = ''.join([
                self.alph[
                    (self.alph.index(sym) -
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
    trit = Trithemius(rus_alph + eng_alph + nums_syms, 2, 5, 3, iterations=7)
    o_text = "Съешь же ещё этих мягких французских булок, да выпей чаю."
    c_txt = trit.encrypt(o_text)
    op_text = trit.decrypt(c_txt)
    print(o_text)
    print(c_txt)
    print(op_text)
    print(trit)
