from Crypto.Hash import SHA256
from Crypto.PublicKey import RSA
from Crypto.Signature import pss
import binascii


class SecurityService():
    def __init__(self) -> None:
        self.keyPair = RSA.generate(1024)

    def get_public_key(self) -> str:
        return self.keyPair.publickey().export_key().decode("utf-8")

    def create_signature(self, client_name: str) -> str:
        message = client_name.encode("utf-8")
        hash = SHA256.new(message)
        signature = pss.new(self.keyPair).sign(hash)
        return str(binascii.b2a_base64(signature), 'utf-8')
