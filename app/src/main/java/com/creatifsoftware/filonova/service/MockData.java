package com.creatifsoftware.filonova.service;

/**
 * Created by kerembalaban on 17.02.2019 at 16:05.
 */
public class MockData {
    public static String carReportListMockData() {
        return "{\n" +
                "  \"equipmentList\": [\n" +
                "    {\n" +
                "      \"equipmentId\": \"123123123\",\n" +
                "      \"plateNumber\": \"34ABC101\",\n" +
                "      \"brandId\": \"123456\",\n" +
                "      \"brandName\": \"Ford\",\n" +
                "      \"modelId\": \"12342134\",\n" +
                "      \"modelName\": \"Focus\",\n" +
                "      \"kmValue\": 10200,\n" +
                "      \"fuelValue\": 4,\n" +
                "      \"equipmentColor\": \"Beyaz\",\n" +
                "      \"groupCodeInformation\": {\n" +
                "        \"groupCodeId\": \"345345\",\n" +
                "        \"groupCodeName\": \"AD\"\n" +
                "      },\n" +
                "      \"equipmentStatus\": {\n" +
                "        \"statusCodeId\": 1,\n" +
                "        \"statusCodeName\": \"Available\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"equipmentId\": \"123123123\",\n" +
                "      \"plateNumber\": \"34ABC102\",\n" +
                "      \"brandId\": \"1234567\",\n" +
                "      \"brandName\": \"Ford\",\n" +
                "      \"modelId\": \"123421347\",\n" +
                "      \"modelName\": \"Focus\",\n" +
                "      \"kmValue\": 220,\n" +
                "      \"fuelValue\": 7,\n" +
                "      \"equipmentColor\": \"Beyaz\",\n" +
                "      \"groupCodeInformation\": {\n" +
                "        \"groupCodeId\": \"53243\",\n" +
                "        \"groupCodeName\": \"AD\"\n" +
                "      },\n" +
                "      \"equipmentStatus\": {\n" +
                "        \"statusCodeId\": 1,\n" +
                "        \"statusCodeName\": \"Available\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"equipmentId\": \"123123123\",\n" +
                "      \"plateNumber\": \"34ABC103\",\n" +
                "      \"brandId\": \"12345678\",\n" +
                "      \"brandName\": \"Ford\",\n" +
                "      \"modelId\": \"123421347\",\n" +
                "      \"modelName\": \"Focus\",\n" +
                "      \"kmValue\": 1375,\n" +
                "      \"fuelValue\": 8,\n" +
                "      \"equipmentColor\": \"Siyah\",\n" +
                "      \"groupCodeInformation\": {\n" +
                "        \"groupCodeId\": \"57567\",\n" +
                "        \"groupCodeName\": \"AD\"\n" +
                "      },\n" +
                "      \"equipmentStatus\": {\n" +
                "        \"statusCodeId\": 2,\n" +
                "        \"statusCodeName\": \"In Washing\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"equipmentId\": \"123123123\",\n" +
                "      \"plateNumber\": \"11ABC01\",\n" +
                "      \"brandId\": \"12345678\",\n" +
                "      \"brandName\": \"BMW\",\n" +
                "      \"modelId\": \"123421347\",\n" +
                "      \"modelName\": \"3.20\",\n" +
                "      \"kmValue\": 2578,\n" +
                "      \"fuelValue\": 2,\n" +
                "      \"equipmentColor\": \"Mavi\",\n" +
                "      \"groupCodeInformation\": {\n" +
                "        \"groupCodeId\": \"1243534536\",\n" +
                "        \"groupCodeName\": \"P\"\n" +
                "      },\n" +
                "      \"equipmentStatus\": {\n" +
                "        \"statusCodeId\": 2,\n" +
                "        \"statusCodeName\": \"In Washing\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"equipmentId\": \"123123123\",\n" +
                "      \"plateNumber\": \"23ABC01\",\n" +
                "      \"brandId\": \"12345678\",\n" +
                "      \"brandName\": \"BMW\",\n" +
                "      \"modelId\": \"123421347\",\n" +
                "      \"modelName\": \"3.20\",\n" +
                "      \"kmValue\": 2578,\n" +
                "      \"fuelValue\": 2,\n" +
                "      \"equipmentColor\": \"Mavi\",\n" +
                "      \"groupCodeInformation\": {\n" +
                "        \"groupCodeId\": \"1243534536\",\n" +
                "        \"groupCodeName\": \"P\"\n" +
                "      },\n" +
                "      \"equipmentStatus\": {\n" +
                "        \"statusCodeId\": 3,\n" +
                "        \"statusCodeName\": \"In Fuel Filling\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"equipmentId\": \"123123123\",\n" +
                "      \"plateNumber\": \"35ABC01\",\n" +
                "      \"brandId\": \"12345678\",\n" +
                "      \"brandName\": \"Kia\",\n" +
                "      \"modelId\": \"123421347\",\n" +
                "      \"modelName\": \"Sportage\",\n" +
                "      \"kmValue\": 2578,\n" +
                "      \"fuelValue\": 4,\n" +
                "      \"equipmentColor\": \"Siyah\",\n" +
                "      \"groupCodeInformation\": {\n" +
                "        \"groupCodeId\": \"1243534536\",\n" +
                "        \"groupCodeName\": \"OD\"\n" +
                "      },\n" +
                "      \"equipmentStatus\": {\n" +
                "        \"statusCodeId\": 4,\n" +
                "        \"statusCodeName\": \"Rental\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }

    public static String damageListMockData() {
        return "{\n" +
                "  \"damageList\": [\n" +
                "    {\n" +
                "      \"damageId\": \"6545435432545\",\n" +
                "      \"damagePartCode\": \"21321312\",\n" +
                "      \"damagePartName\": \"Ön Tampon Süpürgelik\",\n" +
                "      \"damagePhoto\": \"5643245\",\n" +
                "      \"damageTimestamp\": 1552056600,\n" +
                "      \"damageSizeId\": \"1250\",\n" +
                "      \"damageSizeName\": \"Minör\",\n" +
                "      \"damageTypeCode\": \"3213213\",\n" +
                "      \"damageTypeName\": \"Sürtme\",\n" +
                "      \"damageCreatedBy\": {\n" +
                "        \"branchId\": \"12312312\",\n" +
                "        \"branchName\": \"Ordu Ofis\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"damageId\": \"6545435432545\",\n" +
                "      \"damagePartCode\": \"21321312\",\n" +
                "      \"damagePartName\": \"Sol Ön Kapı\",\n" +
                "      \"damagePhoto\": \"5643245\",\n" +
                "      \"damageTimestamp\": 1551879000,\n" +
                "      \"damageSizeId\": \"1250\",\n" +
                "      \"damageSizeName\": \"Orta\",\n" +
                "      \"damageTypeCode\": \"3213213\",\n" +
                "      \"damageTypeName\": \"Ezik\",\n" +
                "      \"damageCreatedBy\": {\n" +
                "        \"branchId\": \"12312313\",\n" +
                "        \"branchName\": \"İstanbul Atatürk İç Hatlar\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"damageId\": \"6545435432545\",\n" +
                "      \"damagePartCode\": \"21321312\",\n" +
                "      \"damagePartName\": \"Sol Ön Kapı\",\n" +
                "      \"damagePhoto\": \"5643245\",\n" +
                "      \"damageTimestamp\": 1551879000,\n" +
                "      \"damageSizeId\": \"1250\",\n" +
                "      \"damageSizeName\": \"Orta\",\n" +
                "      \"damageTypeCode\": \"3213213\",\n" +
                "      \"damageTypeName\": \"Ezik\",\n" +
                "      \"damageCreatedBy\": {\n" +
                "        \"branchId\": \"12312313\",\n" +
                "        \"branchName\": \"İstanbul Atatürk İç Hatlar\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"damageId\": \"6545435432545\",\n" +
                "      \"damagePartCode\": \"21321312\",\n" +
                "      \"damagePartName\": \"Sol Ön Kapı\",\n" +
                "      \"damagePhoto\": \"5643245\",\n" +
                "      \"damageTimestamp\": 1551879000,\n" +
                "      \"damageSizeId\": \"1250\",\n" +
                "      \"damageSizeName\": \"Orta\",\n" +
                "      \"damageTypeCode\": \"3213213\",\n" +
                "      \"damageTypeName\": \"Ezik\",\n" +
                "      \"damageCreatedBy\": {\n" +
                "        \"branchId\": \"12312313\",\n" +
                "        \"branchName\": \"İstanbul Atatürk İç Hatlar\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"damageId\": \"65454354325455\",\n" +
                "      \"damagePartCode\": \"213213123\",\n" +
                "      \"damagePartName\": \"Sürücü Yan Ayna\",\n" +
                "      \"damagePhoto\": \"5643245\",\n" +
                "      \"damageTimestamp\": 1551870000,\n" +
                "      \"damageSizeId\": \"1250\",\n" +
                "      \"damageSizeName\": \"Minör\",\n" +
                "      \"damageTypeCode\": \"3213213\",\n" +
                "      \"damageTypeName\": \"Kırık\",\n" +
                "      \"damageCreatedBy\": {\n" +
                "        \"branchId\": \"12312313\",\n" +
                "        \"branchName\": \"İStanbul Atatürk İç Hatlar\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }

    public static String contractListMockData() {
        return "{\n" +
                "  \"deliveryContractList\": [\n" +
                "    {\n" +
                "      \"contractNumber\": \"23432432\",\n" +
                "      \"pickupTimestamp\": 4354353435435,\n" +
                "      \"dropoffTimestamp\": 324324324,\n" +
                "      \"statusCode\": \"1\",\n" +
                "      \"statusName\": \"Teslimat Bekliyor\",\n" +
                "      \"pickupBranch\": {\n" +
                "        \"branchId\": \"123\",\n" +
                "        \"branchName\": \"Istanbul Atatürk Havalimanı\"\n" +
                "      },\n" +
                "      \"dropoffBranch\": {\n" +
                "        \"branchId\": \"123\",\n" +
                "        \"branchName\": \"Istanbul Atatürk Havalimanı\"\n" +
                "      },\n" +
                "      \"customer\": {\n" +
                "        \"customerId\": \"12345\",\n" +
                "        \"fullName\": \"Kerem Balaban\"\n" +
                "      },\n" +
                "      \"groupCodeInformation\": {\n" +
                "        \"groupCode\": \"A\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"contractNumber\": \"546546\",\n" +
                "      \"pickupTimestamp\": 4354353435435,\n" +
                "      \"dropoffTimestamp\": 324324324,\n" +
                "      \"statusCode\": \"1\",\n" +
                "      \"statusName\": \"Teslimat Bekliyor\",\n" +
                "      \"pickupBranch\": {\n" +
                "        \"branchId\": \"123\",\n" +
                "        \"branchName\": \"Istanbul Atatürk Havalimanı\"\n" +
                "      },\n" +
                "      \"dropoffBranch\": {\n" +
                "        \"branchId\": \"123\",\n" +
                "        \"branchName\": \"Istanbul Atatürk Havalimanı\"\n" +
                "      },\n" +
                "      \"customer\": {\n" +
                "        \"customerId\": \"12345\",\n" +
                "        \"fullName\": \"Ceyhun Kalın\"\n" +
                "      },\n" +
                "      \"groupCodeInformation\": {\n" +
                "        \"groupCode\": \"A\"\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"collectContractList\": [\n" +
                "    {\n" +
                "      \"contractNumber\": \"23432432\",\n" +
                "      \"pickupTimestamp\": 4354353435435,\n" +
                "      \"dropoffTimestamp\": 324324324,\n" +
                "      \"statusCode\": \"1\",\n" +
                "      \"statusName\": \"İade Bekliyor\",\n" +
                "      \"pickupBranch\": {\n" +
                "        \"branchId\": \"123\",\n" +
                "        \"branchName\": \"Istanbul Atatürk Havalimanı\"\n" +
                "      },\n" +
                "      \"dropoffBranch\": {\n" +
                "        \"branchId\": \"123\",\n" +
                "        \"branchName\": \"Istanbul Atatürk Havalimanı\"\n" +
                "      },\n" +
                "      \"customer\": {\n" +
                "        \"customerId\": \"12345\",\n" +
                "        \"fullName\": \"Kerem Balaban\"\n" +
                "      },\n" +
                "      \"groupCodeInformation\": {\n" +
                "        \"groupCode\": \"A\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"contractNumber\": \"546546\",\n" +
                "      \"pickupTimestamp\": 4354353435435,\n" +
                "      \"dropoffTimestamp\": 324324324,\n" +
                "      \"statusCode\": \"1\",\n" +
                "      \"statusName\": \"İade Bekliyor\",\n" +
                "      \"pickupBranch\": {\n" +
                "        \"branchId\": \"123\",\n" +
                "        \"branchName\": \"Istanbul Atatürk Havalimanı\"\n" +
                "      },\n" +
                "      \"dropoffBranch\": {\n" +
                "        \"branchId\": \"123\",\n" +
                "        \"branchName\": \"Istanbul Atatürk Havalimanı\"\n" +
                "      },\n" +
                "      \"customer\": {\n" +
                "        \"customerId\": \"12345\",\n" +
                "        \"fullName\": \"Polat Aydın\"\n" +
                "      },\n" +
                "      \"groupCodeInformation\": {\n" +
                "        \"groupCode\": \"A\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }

    public static String contractInformationMockData() {
        return "{\n" +
                "      \"contractNumber\": \"23432432\",\n" +
                "      \"pickupTimestamp\": 4354353435435,\n" +
                "      \"dropoffTimestamp\": 324324324,\n" +
                "      \"statusCode\": \"1\",\n" +
                "      \"statusName\": \"İade Bekliyor\",\n" +
                "      \"pickupBranch\": {\n" +
                "        \"branchId\": \"123\",\n" +
                "        \"branchName\": \"Istanbul Atatürk Havalimanı\"\n" +
                "      },\n" +
                "      \"dropoffBranch\": {\n" +
                "        \"branchId\": \"123\",\n" +
                "        \"branchName\": \"Istanbul Atatürk Havalimanı\"\n" +
                "      },\n" +
                "      \"customer\": {\n" +
                "        \"customerId\": \"12345\",\n" +
                "        \"fullName\": \"Kerem Balaban\",\n" +
                "        \"mobilePhone\": \"0536 267 82 60\",\n" +
                "        \"email\": \"kerem.balaban@creatifsoftware.com\",\n" +
                "        \"drivingLicenseNumber\": \"18244\"\n" +
                "      },\n" +
                "      \"groupCodeInformation\": {\n" +
                "        \"groupCodeId\": \"1234\",\n" +
                "        \"groupCodeName\": \"A\",\n" +
                "        \"transmissionName\":\"Otomatik\",\n" +
                "        \"fuelType\":\"Benzin\",\n" +
                "        \"seatNumber\":\"5\",\n" +
                "        \"doorNumber\":\"4\",\n" +
                "        \"luggageNumber\":\"2\"\n" +
                "      }\n" +
                "    }";
    }

    public static String equipmentListMockData() {
        return "[\n" +
                "    {\n" +
                "      \"equipmentId\": \"23432432\",\n" +
                "      \"plateNumber\": \"34 ABC 108\",\n" +
                "      \"brandId\":\"123123213\",\n" +
                "      \"brandName\":\"Kia\",\n" +
                "      \"modelId\":\"123123\",\n" +
                "      \"modelName\":\"Sportage\",\n" +
                "      \"equipmentColor\":\"Beyaz\",\n" +
                "      \"fuelValue\":\"8\",\n" +
                "      \"kmValue\":\"7.800\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"equipmentId\": \"234324324\",\n" +
                "      \"plateNumber\": \"34 ABC 109\",\n" +
                "      \"brandId\":\"123123213\",\n" +
                "      \"brandName\":\"Ford\",\n" +
                "      \"modelId\":\"123123\",\n" +
                "      \"modelName\":\"Kuga\",\n" +
                "      \"equipmentColor\":\"Siyah\",\n" +
                "      \"fuelValue\":\"7\",\n" +
                "      \"kmValue\":\"7.900\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"equipmentId\": \"234324323\",\n" +
                "      \"plateNumber\": \"34 ABC 110\",\n" +
                "      \"brandId\":\"123123213\",\n" +
                "      \"brandName\":\"Volkswagen\",\n" +
                "      \"modelId\":\"123123\",\n" +
                "      \"modelName\":\"Tiguan\",\n" +
                "      \"equipmentColor\":\"Kırmızı\",\n" +
                "      \"fuelValue\":\"6\",\n" +
                "      \"kmValue\":\"5.900\"\n" +
                "    }\n" +
                "  ]";
    }

    public static String additionalProductListMockData() {
        return "[\n" +
                "  {\n" +
                "    \"additionProductGuid\": \"234324323\",\n" +
                "    \"additionProductId\": \"54343543\",\n" +
                "    \"additionProductName\": \"Navigasyon\",\n" +
                "    \"maxQuantity\": 1,\n" +
                "    \"selectedQuantity\": 0,\n" +
                "    \"price\": 20,\n" +
                "    \"currency\": \"TL\",\n" +
                "    \"infoText\": \"Deneme\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"additionProductGuid\": \"234324324\",\n" +
                "    \"additionProductId\": \"54343544\",\n" +
                "    \"additionProductName\": \"Bebek Arabası\",\n" +
                "    \"maxQuantity\": 2,\n" +
                "    \"selectedQuantity\": 0,\n" +
                "    \"price\": 30,\n" +
                "    \"currency\": \"TL\",\n" +
                "    \"infoText\": \"Deneme\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"additionProductGuid\": \"234324324\",\n" +
                "    \"additionProductId\": \"54343544\",\n" +
                "    \"additionProductName\": \"Çocuk Koltuğu\",\n" +
                "    \"maxQuantity\": 2,\n" +
                "    \"selectedQuantity\": 0,\n" +
                "    \"price\": 40,\n" +
                "    \"currency\": \"TL\",\n" +
                "    \"infoText\": \"Deneme\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"additionProductGuid\": \"234324324\",\n" +
                "    \"additionProductId\": \"54343544\",\n" +
                "    \"additionProductName\": \"Kar Lastiği\",\n" +
                "    \"maxQuantity\": 2,\n" +
                "    \"selectedQuantity\": 0,\n" +
                "    \"price\": 80,\n" +
                "    \"currency\": \"TL\",\n" +
                "    \"infoText\": \"Deneme\"\n" +
                "  }\n" +
                "]";
    }

    public static String finePriceMockData() {
        return "{\n" +
                "  \"tollAmountList\": [\n" +
                "    {\n" +
                "      \"finePriceId\": \"6545435432543\",\n" +
                "      \"finePriceName\": \"HGS Borcu\",\n" +
                "      \"fineAmount\": 34.7\n" +
                "    },\n" +
                "    {\n" +
                "      \"finePriceId\": \"6545435432544\",\n" +
                "      \"finePriceName\": \"Hasar Bedeli\",\n" +
                "      \"fineAmount\": 250\n" +
                "    },\n" +
                "    {\n" +
                "      \"finePriceId\": \"6545435432545\",\n" +
                "      \"finePriceName\": \"Ek Bedeller\",\n" +
                "      \"fineAmount\": 48.5\n" +
                "    }\n" +
                "  ],\n" +
                "  \"fineAmountList\": [\n" +
                "    {\n" +
                "      \"finePriceId\": \"6545435432543\",\n" +
                "      \"finePriceName\": \"Hız Aşımı\",\n" +
                "      \"fineAmount\": 124.7\n" +
                "    },\n" +
                "    {\n" +
                "      \"finePriceId\": \"6545435432544\",\n" +
                "      \"finePriceName\": \"Telefonla Konuşma\",\n" +
                "      \"fineAmount\": 180\n" +
                "    },\n" +
                "    {\n" +
                "      \"finePriceId\": \"6545435432545\",\n" +
                "      \"finePriceName\": \"Kırmızı Geçiş İhlali\",\n" +
                "      \"fineAmount\": 48.5\n" +
                "    }\n" +
                "  ],\n" +
                "  \"extraPaymentList\": [\n" +
                "    {\n" +
                "      \"finePriceId\": \"6545435432543\",\n" +
                "      \"finePriceName\": \"Eksik Yakıt Bedeli\",\n" +
                "      \"fineAmount\": 34.7\n" +
                "    },\n" +
                "    {\n" +
                "      \"finePriceId\": \"6545435432544\",\n" +
                "      \"finePriceName\": \"Yakıt Dolum Hizmeti\",\n" +
                "      \"fineAmount\": 250\n" +
                "    },\n" +
                "    {\n" +
                "      \"finePriceId\": \"6545435432545\",\n" +
                "      \"finePriceName\": \"Geç Getirme Bedeli\",\n" +
                "      \"fineAmount\": 48.5\n" +
                "    }\n" +
                "  ],\n" +
                "  \"damageAmount\": {\n" +
                "    \"finePriceId\": \"6545435432543\",\n" +
                "    \"finePriceName\": \"Hasar Bedeli\",\n" +
                "    \"fineAmount\": 34.7\n" +
                "  }\n" +
                "}";
    }


    public static String getBranchId() {
        return "A78D5D16-17B1-E811-9410-000C293D97F8";
    }
}
