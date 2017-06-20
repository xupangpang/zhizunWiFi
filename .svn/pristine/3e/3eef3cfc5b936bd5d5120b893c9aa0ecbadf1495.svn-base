package com.zhizun.zhizunwifi.utils;

import java.util.Random;

/**
 * @author xupp
 * @date 2017/4/26
 */

public class RandomGenerator {
    private static final Random RANDOM = new Random();

    // 区间随机
    public float getRandom(float lower, float upper) {
        float min = Math.min(lower, upper);
        float max = Math.max(lower, upper);
        return getRandom(max - min) + min;
    }

    // 上界随机
    public float getRandom(float upper) {
        return RANDOM.nextFloat() * upper;
    }

    // 上界随机
    public int getRandom(int upper) {
        return RANDOM.nextInt(upper);
    }
}
