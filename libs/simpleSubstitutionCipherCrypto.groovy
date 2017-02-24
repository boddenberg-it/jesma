// a simple substitution cipher for hex values
def encrypt(plain, cipher, hexAlphabet) {

    encrypted = ""

    plain.each() {
      encrypted += cipher.charAt(hexAlphabet.indexOf(it))
    }

    encrypted
}

def decrypt(secret, cipher, hexAlphabet) {
    encrypt(secret, hexAlphabet, cipher)
}

// Salt to avoid exposing pattern of secret
def putSalt(encrypted, cipher) {

    def parts = getPartsBySaltPositions(encrypted,
                    getSaltPositionByCipher(cipher))

    Random rng = new Random();

    parts[0] + cipher.charAt(rng.nextInt(16)) +
    parts[1] + cipher.charAt(rng.nextInt(16)) +
    parts[2] + cipher.charAt(rng.nextInt(16)) +
    parts[3] + cipher.charAt(rng.nextInt(16)) +
    parts[4]
}

def removeSalt(encrypted, cipher) {

  positions = getSaltPositionByCipher(cipher)

  encrypted.substring(0, positions[0]) +
  encrypted.substring(positions[0] , positions[1]) +
  encrypted.substring(positions[1] , positions[2]) +
  encrypted.substring(positions[2] , positions[3]) +
  encrypted.substring(positions[3] , encrypted.length())
}

// SaltHelper
def getSaltPositionByCipher(cipher) {

  positions = [ cipher.indexOf("e"),
                cipher.indexOf("4") * 2,
                cipher.indexOf("f") * 3,
                cipher.indexOf("0") * 4 ]

  // to avoid adding saltChar at beginning of string
  if (positions[0]) { positions[0] = 1}

  positions
}

def getPartsBySaltPositions(encrypted, positions) {

  [ encrypted.substring(0, positions[0]),
    encrypted.substring(positions[0], positions[1]),
    encrypted.substring(positions[1], positions[2]),
    encrypted.substring(positions[2], positions[3]),
    encrypted.substring(positions[3], encrypted.length()) ]
}

// internal test

def plain = "8cac5581c7cdb869915472bcda5577a7b2497499c5dbafb4d36552131ffa367c"
def cipher = "eb8a629347fc51d0"
def hexAlphabet = "0123456789abcdef"

def encrypted = encrypt(plain, cipher, hexAlphabet)
def decrypted = decrypt(encrypted, cipher, hexAlphabet)

def salted = putSalt(encrypted, cipher)
def unSalted = removeSalt(encrypted, cipher)
def decryptedFromSalt = decrypt(unSalted, cipher, hexAlphabet)

// debugging
println "------- substitution cipher --------"
println plain
println decrypted
println encrypted
println "---------- with salt now -----------"
println salted
println unSalted
println "------ decrypted the unsalted ------"
println plain
println decryptedFromSalt
println "--------------- Ente ---------------"
