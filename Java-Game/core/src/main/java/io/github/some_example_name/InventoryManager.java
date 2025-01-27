package io.github.some_example_name;

import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class InventoryManager
{
    int coinAmount;
    InventoryManager()
    {

    }

    void changeCoinsBy(int change)
    {
        coinAmount+=change;
    }


}
