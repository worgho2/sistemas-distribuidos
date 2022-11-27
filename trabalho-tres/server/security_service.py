class SecurityService():
    def __init__(self) -> None:
        self.privateKey = "TODO"
        self.publicKey = "TODO"

    def get_public_key(self):
        return self.publicKey

    def create_signature(self, client_name):
        return "SIGNATURE"
