eng_alph = "abcdefghijklmnopqrstuvwxyz"
rus_alph = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя"
nums_syms = "1234567890 .,"


class Trithemius:
    def __init__(self, alph, *k_coeffs):
        self.alph = alph
        self.a_len = len(self.alph)

        self.k_coeffs = k_coeffs

    def solve_polynomial(self, x_value):
        res = 0
        for coeff, power in zip(self.k_coeffs, reversed(range(len(self.k_coeffs)))):
            res += coeff * (x_value ** power)
        return res

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

    def encrypt(self, text):
        return ''.join([
            self.alph[(self.alph.index(sym) + self.solve_polynomial(i)) % self.a_len]
            if sym in self.alph else sym
            for i, sym in enumerate(text.lower())
        ])

    def decrypt(self, text):
        return ''.join([
            self.alph[(self.alph.index(sym) - self.solve_polynomial(i)) % self.a_len]
            if sym in self.alph else sym
            for i, sym in enumerate(text.lower())
        ])

    def __str__(self):
        return f"Trithemius cipher with alphabet: {self.alph}\n" \
               f"k formula: {self.create_polynomial_str()}"


if __name__ == '__main__':
    trit = Trithemius(rus_alph + eng_alph + nums_syms, 2, 5, 3)
    o_text = "Съешь же ещё этих мягких француз|||ских булок, да выпей чаю.|"
    c_txt = trit.encrypt(o_text)
    print(o_text)
    print(c_txt)
    print(trit.decrypt(c_txt))
    print(trit)
