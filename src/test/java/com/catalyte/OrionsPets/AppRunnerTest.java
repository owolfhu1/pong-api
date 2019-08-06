package com.catalyte.OrionsPets;
import org.junit.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class AppRunnerTest {
    @Test
    public void appRunnerSadPath() {
        AppRunner.main(new String[0]);
    }
}
