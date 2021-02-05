package com.springboot.browser.util;

import java.util.*;

public abstract class Tools<K, V> {
    public abstract Map<K, V> sortByValue(Map<K, V> map);
}
