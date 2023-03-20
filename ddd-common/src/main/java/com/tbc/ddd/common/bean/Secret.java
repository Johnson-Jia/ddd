package com.tbc.ddd.common.bean;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * 密钥
 *
 * @author Johnson.Jia
 */
@Data
@Builder
public class Secret implements Serializable {

    private static final long serialVersionUID = -4149127136782535232L;

    /**
     * 会话 id
     */
    private String sessionId;

    /**
     * 会话 密钥
     */
    private String secretKey;

}
