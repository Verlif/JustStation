package idea.verlif.juststation.global.rsa;

import idea.verlif.juststation.global.cache.CacheHandler;
import idea.verlif.juststation.global.util.PrintUtils;
import idea.verlif.juststation.global.util.RsaUtils;

import java.security.Key;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 一次性RSA密钥服务。
 * 获取了私钥后，此密钥对销毁。<br/>
 * <p>
 * 服务逻辑为：<br/>
 * 1. 生产密钥Key，此时会生成密钥对，并将私钥以KeyID作为缓存key存入cache中。<br/>
 * 2. 每次需要使用私钥时，就会通过cache中取出来。
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/12/17 14:28
 */
public class OnceRsaService implements RsaService {

    private static final int ID_LENGTH = 8;
    private static final String CACHE_PREFIX = "station:key:";

    private final CacheHandler handler;

    private final RsaConfig config;

    public OnceRsaService(RsaConfig config, CacheHandler handler) {
        this.config = config;
        this.handler = handler;
    }

    @Override
    public synchronized RsaKey genRsaKey() {
        try {
            Map<String, Key> keyMap = RsaUtils.genKeyPair();
            String puk = RsaUtils.getPublicKey(keyMap);
            String prk = RsaUtils.getPrivateKey(keyMap);
            RsaKey key = new RsaKey();
            key.setId(genId());
            key.setPublicKey(puk);
            handler.put(CACHE_PREFIX + key.getId(), prk, config.getExpire(), TimeUnit.MINUTES);
            return key;
        } catch (Exception e) {
            PrintUtils.print(e);
        }
        return null;
    }

    private String genId() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        do {
            sb.delete(0, sb.length());
            for (int i = 0; i < ID_LENGTH; i++) {
                sb.append(random.nextInt(10));
            }
        } while (handler.get(sb.toString()) != null);
        return sb.toString();
    }

    @Override
    public String decryptByPrivateKey(String id, String pass) {
        String realId = CACHE_PREFIX + id;
        String prk = handler.get(realId);
        if (prk == null) {
            return null;
        }
        handler.remove(realId);
        try {
            return RsaUtils.decryptByPrivateKey(pass, prk);
        } catch (Exception e) {
            PrintUtils.print(e);
        }
        return null;
    }

    @Override
    public String encryptByPublicKey(String key, String content) {
        try {
            return RsaUtils.encryptByPublicKey(content, key);
        } catch (Exception e) {
            PrintUtils.print(e);
        }
        return null;
    }

    @Override
    public String decryptByPublicKey(String key, String pass) {
        try {
            return RsaUtils.decryptByPublicKey(pass, key);
        } catch (Exception e) {
            PrintUtils.print(e);
        }
        return null;
    }

    @Override
    public String encryptByPrivateKey(String id, String content) {
        String realId = CACHE_PREFIX + id;
        String prk = handler.get(realId);
        if (prk == null) {
            return null;
        }
        handler.remove(realId);
        try {
            return RsaUtils.encryptByPrivateKey(content, prk);
        } catch (Exception e) {
            PrintUtils.print(e);
        }
        return null;
    }
}
