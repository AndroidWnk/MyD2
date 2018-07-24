package com.etrans.myd2.util;

public class VehicleUtils
{
  public static float getCanRange(float paramFloat, int paramInt)
  {
    if (paramFloat > 50.0F) {
      paramFloat = paramFloat * (180 - 130 * (100 - paramInt) / 50) / (105 - 55 * (100 - paramInt) / 50);
        return paramFloat;
    }
//    while (paramFloat >= 0.1F);
    return paramInt * 180 / 100;//hlj for test
  }

  public static int getOdoSoc(int paramInt1, int paramInt2) {
    switch (paramInt1) {
      case 80:
        if ((paramInt2 >= 10) && (paramInt2 < 50)) {
          return (int) (paramInt2 - 0.25D * (50 - paramInt2));
        } else if ((paramInt2 >= 0) && (paramInt2 < 10)) {
          return 0;
        }
      case 100:
        if ((paramInt2 > 40) && (paramInt2 < 50)) {
          return 50 - 2 * (50 - paramInt2);
        }
        else if ((paramInt2 > 10) && (paramInt2 <= 40)) {
            return paramInt2 - 10;
        } else if ((paramInt2 >= 0) && (paramInt2 <= 10)) {
          return 0;
        }
      default:
        return paramInt2;
    }
  }

  public float getCanRangeKilo(int paramInt)
  {
    return 0.0F;
  }

}
