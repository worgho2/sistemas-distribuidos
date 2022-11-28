from Crypto.Hash import SHA256
from Crypto.PublicKey import RSA
from Crypto.Signature import pss


class SecurityService():
    def __init__(self) -> None:
        self.keyPair = RSA.generate(1024)

    def get_public_key(self):
        return self.keyPair.publickey().export_key()

    def create_signature(self, client_name: str):
        message = client_name.encode("utf-8")
        hash = SHA256.new(message)
        return pss.new(self.keyPair).sign(hash)
