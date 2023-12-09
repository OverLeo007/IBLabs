import hashlib
import unittest
import os
import zlib

from rsa.RSA import RSA


def file_sha256(file_path):
    sha256_hash = hashlib.sha256()
    with open(file_path, "rb") as file:
        for byte_block in iter(lambda: file.read(4096), b""):
            sha256_hash.update(byte_block)
    return sha256_hash.hexdigest()


def file_crc32(file_path):
    with open(file_path, 'rb') as file:
        crc_hash = 0
        while True:
            s = file.read(4096)
            if not s:
                break
            crc_hash = zlib.crc32(s, crc_hash)
        return "%08X" % (crc_hash & 0xFFFFFFFF)


def save_file(path, data: bytes):
    with open(path, "wb") as file:
        file.write(data)


class DynamicTestsMeta(type):
    test_folder = "files/test_files/"
    input_folder = test_folder + "input/"
    encoded_folder = test_folder + "encoded/"
    decoded_folder = test_folder + "decoded/"

    def __new__(mcs, name, bases, dct):
        filenames = {}
        for file in os.listdir(DynamicTestsMeta.input_folder):
            _, extension = os.path.splitext(file)
            extension = extension[1:]
            filenames.setdefault(extension, []).append(file)

        dct["filenames"] = filenames
        for extension, files in filenames.items():
            method_name = f"test_{extension}"

            def test_method(self, files=files):
                for filename in files:
                    inp_filepath = DynamicTestsMeta.input_folder + filename
                    enc_filepath = DynamicTestsMeta.encoded_folder \
                                   + filename + ".rsa"
                    dec_filepath = DynamicTestsMeta.decoded_folder \
                                   + filename.replace(".", ".dec.")

                    enc = self.rsa.encrypt(inp_filepath)
                    save_file(enc_filepath, enc)

                    dec = self.rsa.decrypt(enc_filepath)
                    save_file(dec_filepath, dec)

                    self.assertEqual(file_sha256(inp_filepath),
                                     file_sha256(dec_filepath))
                    self.assertEqual(file_crc32(inp_filepath),
                                     file_crc32(dec_filepath))
                    print(
                        filename,
                        f"inp sha256: {file_sha256(inp_filepath)}",
                        f"out sha256: {file_sha256(dec_filepath)}",
                        f"inp crc: {file_crc32(inp_filepath)}",
                        f"out crc: {file_crc32(dec_filepath)}",
                        "=" * 76,
                        sep="\n"
                    )

            dct[method_name] = test_method

        return super(DynamicTestsMeta, mcs).__new__(mcs, name, bases, dct)


class TestRSA(unittest.TestCase, metaclass=DynamicTestsMeta):

    @classmethod
    def setUpClass(cls):
        if not os.path.exists(TestRSA.encoded_folder):
            os.makedirs(TestRSA.encoded_folder)

        if not os.path.exists(TestRSA.decoded_folder):
            os.makedirs(TestRSA.decoded_folder)
        cls.rsa = RSA()
