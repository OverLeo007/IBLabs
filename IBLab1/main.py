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

        self.keyLineEdit.textChanged.connect(self.update_encoder)

        self.changeModePushButton.clicked.connect(self.change_mode)
        self.encoder = Trithemius(rus_alph + eng_alph + nums_syms, "0")
        self.leftTextBrowser.setFocus()
        self.leftTextBrowser.textChanged.connect(self.update_text)
        self.button_text = "Режим расшифровки"
        self.is_enc_mode = True
        self.update_encoder()


    def update_encoder(self):
        self.encoder.upd_key(self.keyLineEdit.text())
        self.label.setText(str(self.encoder))
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
