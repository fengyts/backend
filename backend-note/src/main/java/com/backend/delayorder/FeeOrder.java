package com.backend.delayorder;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class FeeOrder implements Serializable {

    private static final long serialVersionUID = -8965700067323596831L;

    private String orderNo;
    private LocalDateTime orderTime;

}
