package idea.verlif.juststation.global.rsa;

/**
 * RSA密钥服务，用于管理RSA密钥对。
 * 本服务不向外提供私钥。
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/12/17 14:21
 */
public interface RsaService {

    /**
     * 生产密钥Key
     *
     * @return 密钥Key
     */
    RsaKey genRsaKey();

    /**
     * 生成密钥Key
     *
     * @param id 自定义密钥KeyId；若重复则覆盖
     * @return 密钥Key
     */
    RsaKey genRsaKey(String id);

    /**
     * 私钥解密
     *
     * @param id   密钥KeyId
     * @param pass 密文
     * @return 解密后明文
     */
    String decryptByPrivateKey(String id, String pass);

    /**
     * 公钥加密
     *
     * @param key     公钥
     * @param content 明文
     * @return 加密后密文
     */
    String encryptByPublicKey(String key, String content);

    /**
     * 公钥解密
     *
     * @param key  公钥
     * @param pass 密文
     * @return 解密后明文
     */
    String decryptByPublicKey(String key, String pass);

    /**
     * 私钥加密
     *
     * @param id      密钥KeyID
     * @param content 明文
     * @return 加密后密文
     */
    String encryptByPrivateKey(String id, String content);
}
