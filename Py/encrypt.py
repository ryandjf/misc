import sys
from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes

# python encrypt.py passphrase input_file output_file
passphrase = sys.argv[1] # passphrase, 16 bytes length
#passphrase = 'Sixteen byte key'
input_file = sys.argv[2] # input file name
output_file = sys.argv[3] # output file name

key = str.encode(passphrase)
# key = get_random_bytes(16)
# key = b'Sixteen byte key'

file_in = open(input_file, "rb")
data = bytearray(file_in.read())
file_in.close()

cipher = AES.new(key, AES.MODE_EAX)
ciphertext, tag = cipher.encrypt_and_digest(data)

file_encrypted = open(output_file, "wb")
[ file_encrypted.write(x) for x in (cipher.nonce, tag, ciphertext) ]
file_encrypted.close()
