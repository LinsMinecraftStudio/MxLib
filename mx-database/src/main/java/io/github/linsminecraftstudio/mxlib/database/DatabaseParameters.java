package io.github.linsminecraftstudio.mxlib.database;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DatabaseParameters {
    private int maxPoolSize = 10;
    private boolean autoCommit = true;
    private long idleTimeout = 3000;
}
