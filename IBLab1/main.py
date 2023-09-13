import re
import sys

from trithemius import *
from PyQt5 import QtWidgets
from PyQt5.QtCore import pyqtSignal


from gui import Ui_Form


class MainWindow(QtWidgets.QWidget, Ui_Form):
    coeffs_updated = pyqtSignal(list)

    def __init__(self):
        super(MainWindow, self).__init__()
        self.setupUi(self)
        self.coeffsLineEdit.textChanged.connect(self.check_coeffs)
        self.changeModePushButton.clicked.connect(self.change_mode)
        self.encoder = Trithemius(rus_alph + eng_alph + nums_syms, 0)
        self.coeffs_updated.connect(self.update_encoder)
        self.leftTextBrowser.setFocus()
        self.leftTextBrowser.textChanged.connect(self.update_text)
        print(self.encoder.create_polynomial_str())
        self.button_text = "Режим расшифровки"
        self.is_enc_mode = True

    def check_coeffs(self):
        pattern = r"^-?\d+(\s+-?\d+)*$"
        text = self.coeffsLineEdit.text()
        if re.match(pattern, text) is not None:
            self.coeffs_updated.emit(list(map(int, text.split(" "))))
            self.leftTextBrowser.setReadOnly(False)

        else:
            self.leftTextBrowser.setReadOnly(True)

    def update_encoder(self, coeffs):
        self.encoder.k_coeffs = coeffs
        self.update_text()

    def change_mode(self):
        buf_title = self.leftTextLabel.text()
        self.leftTextLabel.setText(self.rightTextLabel.text())
        self.rightTextLabel.setText(buf_title)

        buf_text = self.leftTextBrowser.toPlainText()
        self.leftTextBrowser.setText(self.rightTextBrowser.toPlainText())
        self.rightTextBrowser.setText(buf_text)

        buf_button_text = self.changeModePushButton.text()
        self.changeModePushButton.setText(self.button_text)
        self.button_text = buf_button_text

        self.is_enc_mode = not self.is_enc_mode

    def update_text(self):
        if self.is_enc_mode:
            self.rightTextBrowser.setText(
                self.encoder.encrypt(self.leftTextBrowser.toPlainText())
            )
        else:
            self.rightTextBrowser.setText(
                self.encoder.decrypt(self.leftTextBrowser.toPlainText())
            )


def main():
    app = QtWidgets.QApplication([])
    application = MainWindow()
    application.show()

    sys.exit(app.exec())


if __name__ == '__main__':
    main()
