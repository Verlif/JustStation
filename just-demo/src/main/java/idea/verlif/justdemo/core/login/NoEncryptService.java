package idea.verlif.justdemo.core.login;

import idea.verlif.juststation.global.rsa.RsaKey;
import idea.verlif.juststation.global.rsa.RsaService;
import org.springframework.stereotype.Component;

/**
 * 不加密的RSA服务。
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/12/17 16:23
 */
@Component
public class NoEncryptService implements RsaService {

    @Override
    public RsaKey genRsaKey() {
        return new RsaKey();
    }

    @Override
    public String decryptByPrivateKey(String id, String pass) {
        return pass;
    }

    @Override
    public String encryptByPublicKey(String key, String content) {
        return content;
    }

    @Override
    public String decryptByPublicKey(String key, String pass) {
        return pass;
    }

    @Override
    public String encryptByPrivateKey(String id, String content) {
        return content;
    }
}
