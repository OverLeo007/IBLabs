from datetime import datetime
from PyQt5.QtWidgets import QFileDialog

from rsa.RSA import RSA
from template import Ui_MainWindow
from PyQt5 import QtWidgets
import sys


class MainWindow(QtWidgets.QMainWindow, Ui_MainWindow):
    def __init__(self):
        super(MainWindow, self).__init__()
        self.setupUi(self)
        self.setWindowTitle("RSA example")
        self.rsa = RSA()
        self.setKeys()
        self.encryptFilesButton.setEnabled(False)
        self.decryptFilesButton.setEnabled(False)
        self.inpFiles = []
        self.inputFilesButton.clicked.connect(self.setInputFiles)
        self.encryptFilesButton.clicked.connect(self.encryptFiles)
        self.decryptFilesButton.clicked.connect(self.decryptFiles)
        self.saveKeysButton.clicked.connect(self.saveKeys)
        self.loadKeysButton.clicked.connect(self.loadKeys)
        self.generateKeysButton.clicked.connect(self.regenKeys)

    def setKeys(self):
        self.nLineEdit.setText(str(self.rsa.N))
        self.eLineEdit.setText(str(self.rsa.e))
        self.sLineEdit.setText(str(self.rsa.s))

    def setInputFiles(self):
        options = QFileDialog.Options()

        file_dialog = QFileDialog()
        file_dialog.setFileMode(
            QFileDialog.ExistingFiles)
        file_dialog.setOptions(options)
        file_dialog.setDirectory(self.rsa.file_folder)

        file_names, _ = file_dialog.getOpenFileNames(self, "Выберите файлы", "",
                                                     "Все файлы (*)")
        if file_names:
            self.inpFiles = list(zip(
                file_names, list(map(lambda x: x.split("/")[-1], file_names))
            ))
        self.changeButtonStates()

    def changeButtonStates(self):
        if not self.inpFiles:
            self.encryptFilesButton.setEnabled(False)
            self.decryptFilesButton.setEnabled(False)
            return
        else:
            self.encryptFilesButton.setEnabled(True)
        if all(map(lambda x: x[1].split(".")[-1] == "rsa", self.inpFiles)):
            self.decryptFilesButton.setEnabled(True)

    def encryptFiles(self):
        for filepath, filename in self.inpFiles:
            file_bytes = self.rsa.encrypt(filepath)
            enc_filepath = self.rsa.enc_folder + filename + ".rsa"
            with open(enc_filepath, "wb") as file:
                file.write(file_bytes)
            self.logTextBrowser.append(f"{filename} закодирован "
                                       f"и сохранен в {enc_filepath}")

    def decryptFiles(self):
        for filepath, filename in self.inpFiles:
            try:
                dec_file_bytes = self.rsa.decrypt(filepath)
            except OverflowError:
                self.logTextBrowser.append(
                    f"Произошла ошибка при расшифровке {filename}, "
                    f"вероятнее всего он зашифрован при помощи других ключей")
                continue

            dec_filepath = self.rsa.dec_folder + filename.replace(
                ".rsa", ""
            ).replace(
                ".", ".dec."
            )
            with open(dec_filepath, "wb") as file:
                file.write(dec_file_bytes)
            self.logTextBrowser.append(f"{filename} раскодирован "
                                       f"и сохранен в {dec_filepath}")

    def regenKeys(self):
        self.rsa = RSA()
        self.setKeys()

    def saveKeys(self):
        filename = f"keys{datetime.now().strftime('%Y-%m-%d %H.%M.%S')}.json"
        self.rsa.save_keys(filename)
        self.logTextBrowser.append(f"Ключи сохранены по адресу "
                                   f"{RSA.key_folder + filename}")

    def loadKeys(self):
        options = QFileDialog.Options()

        file_dialog = QFileDialog()
        file_dialog.setFileMode(
            QFileDialog.ExistingFiles)
        file_dialog.setOptions(options)
        file_dialog.setDirectory(self.rsa.key_folder)

        file_name, _ = file_dialog.getOpenFileName(self, "Выберите файл", "",
                                                   "JSON Files (*.json)")
        if file_name:
            self.rsa.load_keys(file_name)
            self.logTextBrowser.append(
                f"Ключи загружены из файла {file_name.split('/')[-1]}"
            )
        self.setKeys()



def main():
    app = QtWidgets.QApplication([])
    application = MainWindow()
    application.show()

    sys.exit(app.exec())


if __name__ == '__main__':
    main()
