package com.technews.seedData;

import com.technews.seedData.SeedDataLoader;

public class SeedDataRunner {

    public static void SeedRunner(String[] args) {
        SeedDataLoader seedDataLoader = new SeedDataLoader();
        seedDataLoader.loadSeedData();
    }
}