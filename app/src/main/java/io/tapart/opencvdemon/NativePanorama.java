package io.tapart.opencvdemon;

public class NativePanorama {
    public native static void processPanorama(long[] imageAddressArray, long outputAddress);
    public native static void processPanoramaC(long[] imageAddressArray, long outputAddress);
}
