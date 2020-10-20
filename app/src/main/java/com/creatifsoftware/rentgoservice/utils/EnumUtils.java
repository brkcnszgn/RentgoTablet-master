package com.creatifsoftware.rentgoservice.utils;

/**
 * Created by kerembalaban on 4.04.2019 at 03:54.
 */
public class EnumUtils {

    public static final EnumUtils instance = new EnumUtils();

    public enum ContractType {
        INDIVIDUAL(10),
        CORPORATE(20);

        private final int intValue;

        ContractType(int value) {
            intValue = value;
        }

        public int getIntValue() {
            return intValue;
        }

        public String getName(int value) {
            switch (value) {
                case 10:
                    return "Bireysel";
                case 20:
                    return "Kurumsal";
                default:
                    return "";
            }
        }
    }

    public enum PaymentMethod {
        CURRENT(10),
        CREDIT_CARD(20),
        FULL_CREDIT(40),
        PAY_BROKER(50),
        PAY_OFFICE(60);

        private final int intValue;

        PaymentMethod(int value) {
            intValue = value;
        }

        public int getIntValue() {
            return intValue;
        }

        public String getName(int value) {
            switch (value) {
                case 10:
                    return "Bireysel";
                case 20:
                    return "Kurumsal";
                case 40:
                    return "Full Credit";
                case 50:
                    return "Broker-Şimdi Öde";
                case 60:
                    return "Broker-Sonra Öde";
                default:
                    return "";
            }
        }
    }

    public enum ContractStatusCode {
        NEW(1),
        NO_SHOW(100000000),
        RENTAL(100000001),
        WAITING_FOR_DELIVERY(100000002),
        COMPLETED(100000003),
        CUSTOMER_DEMAND(100000007);

        private final int intValue;

        ContractStatusCode(int value) {
            intValue = value;
        }

        public int getIntValue() {
            return intValue;
        }
    }

    public enum TransferStatusCode {
        WAITING_FOR_DELIVERY(100000000),
        TRANSFERRED(100000001);

        private final int intValue;

        TransferStatusCode(int value) {
            intValue = value;
        }

        public int getIntValue() {
            return intValue;
        }
    }

    public enum ContractDebtStatus {
        COMPLETE_WITHOUT_DEBPT(10),
        COMPLETE_WITH_DEBPT(20);

        private final int intValue;

        ContractDebtStatus(int value) {
            intValue = value;
        }

        public int getIntValue() {
            return intValue;
        }
    }

    public enum EquipmentStatusCode {
        NONE(-1),
        AVAILABLE(1),
        RENTAL(100000000),
        MISSING_INVENTORIES(100000001),
        IN_WASHING(100000002),
        IN_FUEL_FILLING(100000003),
        IN_TRANSFER(100000004),
        LOST_STOLEN(100000005),
        WAITING_SECOND_HAND_CONFIRMATION(100000008),
        IN_SERVICE(5);

        private final int intValue;

        EquipmentStatusCode(int value) {
            intValue = value;
        }

        public int getIntValue() {
            return intValue;
        }

        public String getName(int value) {
            switch (value) {
                case -1:
                    return "";
                case 1:
                    return "Kullanılabilir";
                case 100000001:
                    return "Ekipman Eksik";
                case 100000002:
                    return "Yıkamada";
                case 100000003:
                    return "Yakıt Dolumda";
                case 100000000:
                    return "Kirada";
                case 100000004:
                    return "Transfer/ÜB";
                case 100000005:
                    return "Kayıp/Çalıntı";
                case 100000008:
                    return "İkinci El Onayı Bekliyor";
                case 5:
                    return "Serviste";
                default:
                    return "";
            }
        }
    }

    public enum EquipmentChangeType {
        UPGRADE(20),
        DOWNGRADE(30),
        UPSELL(40),
        DOWNSELL(50);

        private final int intValue;

        EquipmentChangeType(int value) {
            intValue = value;
        }

        public int getIntValue() {
            return intValue;
        }
    }

    public enum AdditionalProductType {
        PRODUCT(20),
        SERVICE(10);

        private final int intValue;

        AdditionalProductType(int value) {
            intValue = value;
        }

        public int getIntValue() {
            return intValue;
        }
    }

    public enum DamageDocumentType {
        STATEMENT(1),
        NONE(2),
        POLICE_REPORT(3);

        private final int intValue;

        DamageDocumentType(int value) {
            intValue = value;
        }

        public int getIntValue() {
            return intValue;
        }
    }

    public enum TransactionType {
        SALE(10),
        REFUND(20),
        DEPOSIT(30);

        private final int intValue;

        TransactionType(int value) {
            intValue = value;
        }

        public int getIntValue() {
            return intValue;
        }
    }

    public enum TransferType {
        NONE(0),
        DAMAGE(10),
        SERVICE(20),
        FAULT(30),
        SECOND_HAND(40),
        BRANCH(50),
        FREE(60),
        FIRST_TRANSFER(70);

        private final int intValue;
        private String name;

        TransferType(int value) {
            intValue = value;
        }

        public int getIntValue() {
            return intValue;
        }

        public String getName(int value) {
            switch (value) {
                case 10:
                    return "Servis-Hasar";
                case 20:
                    return "Servis-Bakım";
                case 30:
                    return "Servis-Arıza";
                case 40:
                    return "İkinci El";
                case 50:
                    return "Şubeye Transfer";
                case 60:
                    return "Ücretsiz Bilet";
                case 70:
                    return "İlk Transfer";
                default:
                    return "";
            }
        }
    }
}
