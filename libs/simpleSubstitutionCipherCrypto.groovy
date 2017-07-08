// a simple substitution cipher for hex values

public String encryptJnlpSecret(secret, cipher, salt) {
  putSalt(encrypt(secret, cipher), cipher, salt)
}

public String decryptJnlpSecret(encrypted, cipher, salt) {
  decrypt(removeSalt(encrypted, cipher, salt), cipher)
}

private String encrypt(plain, cipher, hexAlphabet = "0123456789abcdef") {

    encrypted = ""

    plain.each() {
      encrypted += cipher.charAt(hexAlphabet.indexOf(it))
    }

    encrypted
}

private decrypt(encrypted, cipher) {
    encrypt(encrypted, "0123456789abcdef", cipher)
}

// Salt to avoid exposing pattern of secret
private putSalt(encrypted, cipher, salt) {

    parts = getPartsBySaltPositions(encrypted,
                    getSaltPositions(encrypted.substring(48,64) + cipher, salt))

    parts[0] + encrypted.charAt(4) +
    parts[1] + encrypted.charAt(16) +
    parts[2] + encrypted.charAt(32) +
    parts[3] + encrypted.charAt(63) +
    parts[4]
}

private removeSalt(encrypted, cipher, salt) {

  positions = getSaltPositions(encrypted.substring(52,68) + cipher, salt)

  encrypted.substring(0, positions[0]) +
  encrypted.substring(positions[0] + 1 , positions[1] + 1) +
  encrypted.substring(positions[1] + 2 , positions[2] + 2) +
  encrypted.substring(positions[2] + 3 , positions[3] + 3) +
  encrypted.substring(positions[3] + 4 , encrypted.length())
}

// SaltHelper
private getSaltPositions(cipher, salt) {

  positions = []

  (0..3).each() {
      positions << (int) Math.round(cipher.indexOf(salt[it]) * 1.5)
  }

  positions.sort()
}

private getPartsBySaltPositions(encrypted, positions) {

  [ encrypted.substring(0, positions[0]),
    encrypted.substring(positions[0], positions[1]),
    encrypted.substring(positions[1], positions[2]),
    encrypted.substring(positions[2], positions[3]),
    encrypted.substring(positions[3], encrypted.length()) ]
}


/* INTERNAL TESTING */

// check avoiding of adding salt at the beginning of the string
test(getRandomHex(), "eb8a629347fc51d0", getRandomSalt())
// generic test(s)
(1..25).each() { test(getRandomHex(), "eb8a629347fc51d0", "4907") }


// TODO: break down test method in three ones
def test(plain, cipher, salt) {
  // testing substition ciphering (encrypt() decrypt() methods)
  def encrypted = encrypt(plain, cipher)
  def encrypted1 = encrypt(plain, cipher)
  def encrypted2 = encrypt(plain, cipher)
  def decrypted = decrypt(encrypted, cipher)

  println "\n------------ test simpleSubstitutionCipherCrypto.groovy --------\n"
  println "plain:       " + plain
  println "decrypted:   " + decrypted
  println "encrypted:   " + encrypted

  assert decrypted.equals(plain)
  assert !encrypted.equals(plain)
  assert encrypted.length() == plain.length()
  assert encrypted.equals(encrypted2) && encrypted1.equals(encrypted2)

  // testing salting
  def salted = putSalt(encrypted, cipher, salt)
  def salted1 = putSalt(encrypted, cipher, salt)
  def salted2 = putSalt(encrypted, cipher, salt)
  def unsalted = removeSalt(salted, cipher, salt)
  def decryptedFromSalt = decrypt(unsalted, cipher)

  println "\n------------ decrypted the unsalted\n"
  println "plain:       " + plain
  println "saltDecrypt: " + decryptedFromSalt
  println "salted:      " + salted
  println "unsalted:    " + unsalted

  assert plain.equals(decryptedFromSalt)
  assert unsalted.equals(encrypted)
  assert salted.length() == encrypted.length() + 4
  assert !salted.contains(encrypted)
  assert !salted.equals(unsalted) && !salted.equals(encrypted)
  assert salted.equals(salted2) && salted1.equals(salted2)

  // testing public methods encryptedJnlp() and decryptedJnlp()
  def encryptedJnlp = encryptJnlpSecret(plain, cipher, salt)
  def decryptedJnlp = decryptJnlpSecret(encryptedJnlp, cipher, salt)

  println "\n------------ de/encrypt jnlp public methods\n"
  println "encryptJnlp: " + encryptedJnlp
  println "decryptJnlp: " + decryptedJnlp
  println "plain:       " + plain

  assert decryptedJnlp.equals(plain)
  assert encryptedJnlp.length() == decryptedJnlp.length() + 4
  assert !encryptedJnlp.contains(encrypted)

  println "\n------------- end of substitution cipher test ------------------\n"
}

// test helper functions
def getRandomHex(length = 64){

  hexAlphabet = "0123456789abcdef"
  hexAlphabet_length = hexAlphabet.length()

  random = new Random()
  randomHex = ""

  (1..length).each {
    rand = random.nextInt(hexAlphabet_length)
    randomHex += hexAlphabet.charAt(rand)
  }

  randomHex
}

def getRandomCipher() {
    getRandomHex(1024).toList().unique().join()
}

def getRandomSalt() {
    getRandomCipher().substring(0,4).toList()
}
