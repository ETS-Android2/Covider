package com.cs310.covider.model;
import com.cs310.covider.fragment.FormFragment;
import com.cs310.covider.fragment.CheckInFormFragment;

public class RiskCalculator {
    public static double getRiskAtBuilding(String buildingAbbrev) {
        Integer totalCheckedIn = CheckInFormFragment.map.get(buildingAbbrev.toUpperCase());
        if (totalCheckedIn != null) {


        }
        return 0;
    }
}
