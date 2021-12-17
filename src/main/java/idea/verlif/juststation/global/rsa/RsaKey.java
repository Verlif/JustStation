package idea.verlif.juststation.global.rsa;

/**
 * RSA密钥对ID
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/12/17 14:32
 */
public class RsaKey {

    /**
     * 密钥对ID
     */
    private String id;

    /**
     * 公钥
     */
    private String publicKey;

    public String getId() {
        return id;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
