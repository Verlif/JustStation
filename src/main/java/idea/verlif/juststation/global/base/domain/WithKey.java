package idea.verlif.juststation.global.base.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import idea.verlif.juststation.global.rsa.RsaKey;

import javax.validation.constraints.NotBlank;

/**
 * 附带密钥ID对象
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/12/17 15:30
 */
public class WithKey {

    @JsonIgnore
    @NotBlank
    private String keyId;

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public void withKey(RsaKey key) {
        this.keyId = key.getId();
    }
}
