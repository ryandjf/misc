import sys
from Crypto.Cipher import AES

# python decrypt.py passphrase input_file output_file
passphrase = sys.argv[1] # passphrase, 16 bytes length
#passphrase = 'Sixteen byte key'
input_file = sys.argv[2] # input file name
output_file = sys.argv[3] # output file name

file_in = open(input_file, "rb")
nonce, tag, ciphertext = [ file_in.read(x) for x in (16, 16, -1) ]

key = str.encode(passphrase)

cipher = AES.new(key, AES.MODE_EAX, nonce)
data = cipher.decrypt_and_verify(ciphertext, tag)

file_decrypted = open(output_file, "wb")
file_decrypted.write(data) 
file_decrypted.close()
