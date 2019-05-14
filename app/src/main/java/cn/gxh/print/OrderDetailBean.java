package cn.gxh.print;

import java.io.Serializable;
import java.util.List;

public class OrderDetailBean extends BaseBean implements Serializable {


    /**
     * result : {"orderState":0,"orderStateStr":"待付款","orderNumber":"E20181113143808178800052","creationTime":"2018-11-13 14:38:08","creationTimeStr":"2018-11-13 14:38:08","creatorUserPhone":"13521870263","commdityDetailsList":[{"goodName":"","goodCode":"","originalUnitPrice":765,"presentUnitPrice":0,"count":2}],"amountCount":2,"amountPrice":765,"amountPriceStr":"765.00","amountReduce":0,"amountReduceStr":"0.00","payType":0,"payTypeStr":"未支付","presentPrice":0,"presentPriceStr":"0.00","originalPrice":1530,"originalPriceStr":"1530.00","oddChange":"1530.00","id":"f03289f0-0e89-4668-9330-39580cd10450"}
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable {
        /**
         * orderState : 0
         * orderStateStr : 待付款
         * orderNumber : E20181113143808178800052
         * creationTime : 2018-11-13 14:38:08
         * creationTimeStr : 2018-11-13 14:38:08
         * creatorUserPhone : 13521870263
         * commdityDetailsList : [{"goodName":"","goodCode":"","originalUnitPrice":765,"presentUnitPrice":0,"count":2}]
         * amountCount : 2
         * amountPrice : 765.0
         * amountPriceStr : 765.00
         * amountReduce : 0.0
         * amountReduceStr : 0.00
         * payType : 0
         * payTypeStr : 未支付
         * presentPrice : 0.0
         * presentPriceStr : 0.00
         * originalPrice : 1530.0
         * originalPriceStr : 1530.00
         * oddChange : 1530.00
         * id : f03289f0-0e89-4668-9330-39580cd10450
         */

        private int orderState;
        private String orderStateStr;
        private String orderNumber;
        private String creationTime;
        private String creationTimeStr;
        private String creatorUserPhone;
        private int amountCount;
        private double amountPrice;
        private String amountPriceStr;
        private double amountReduce;
        private String amountReduceStr;
        private int payType;
        private String payTypeStr;
        private double presentPrice;
        private String presentPriceStr;
        private double originalPrice;
        private String originalPriceStr;
        private String oddChange;
        private String id;
        private String customerPhone;
        private List<CommdityDetailsListBean> commdityDetailsList;

        //实收
        private String actualPrice;
        private String actualPriceStr;

        private String rewardIntegral;
        private String consumeIntegral;

        private int integralAmount;

        //----------------------------2019.1.11保护期积分
        private boolean isIntegralProtect;


        private List<SourceListBean> sourceList;

        public List<SourceListBean> getSourceList() {
            return sourceList;
        }

        public void setSourceList(List<SourceListBean> sourceList) {
            this.sourceList = sourceList;
        }

        public boolean isIntegralProtect() {
            return isIntegralProtect;
        }

        public void setIntegralProtect(boolean integralProtect) {
            isIntegralProtect = integralProtect;
        }

        public int getIntegralAmount() {
            return integralAmount;
        }

        public void setIntegralAmount(int integralAmount) {
            this.integralAmount = integralAmount;
        }

        public String getCustomerPhone() {
            return customerPhone;
        }

        public void setCustomerPhone(String customerPhone) {
            this.customerPhone = customerPhone;
        }

        public String getRewardIntegral() {
            return rewardIntegral;
        }

        public void setRewardIntegral(String rewardIntegral) {
            this.rewardIntegral = rewardIntegral;
        }

        public String getConsumeIntegral() {
            return consumeIntegral;
        }

        public void setConsumeIntegral(String consumeIntegral) {
            this.consumeIntegral = consumeIntegral;
        }

        public String getActualPrice() {
            return actualPrice;
        }

        public void setActualPrice(String actualPrice) {
            this.actualPrice = actualPrice;
        }

        public String getActualPriceStr() {
            return actualPriceStr;
        }

        public void setActualPriceStr(String actualPriceStr) {
            this.actualPriceStr = actualPriceStr;
        }

        public int getOrderState() {
            return orderState;
        }

        public void setOrderState(int orderState) {
            this.orderState = orderState;
        }

        public String getOrderStateStr() {
            return orderStateStr;
        }

        public void setOrderStateStr(String orderStateStr) {
            this.orderStateStr = orderStateStr;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(String creationTime) {
            this.creationTime = creationTime;
        }

        public String getCreationTimeStr() {
            return creationTimeStr;
        }

        public void setCreationTimeStr(String creationTimeStr) {
            this.creationTimeStr = creationTimeStr;
        }

        public String getCreatorUserPhone() {
            return creatorUserPhone;
        }

        public void setCreatorUserPhone(String creatorUserPhone) {
            this.creatorUserPhone = creatorUserPhone;
        }

        public int getAmountCount() {
            return amountCount;
        }

        public void setAmountCount(int amountCount) {
            this.amountCount = amountCount;
        }

        public double getAmountPrice() {
            return amountPrice;
        }

        public void setAmountPrice(double amountPrice) {
            this.amountPrice = amountPrice;
        }

        public String getAmountPriceStr() {
            return amountPriceStr;
        }

        public void setAmountPriceStr(String amountPriceStr) {
            this.amountPriceStr = amountPriceStr;
        }

        public double getAmountReduce() {
            return amountReduce;
        }

        public void setAmountReduce(double amountReduce) {
            this.amountReduce = amountReduce;
        }

        public String getAmountReduceStr() {
            return amountReduceStr;
        }

        public void setAmountReduceStr(String amountReduceStr) {
            this.amountReduceStr = amountReduceStr;
        }

        public int getPayType() {
            return payType;
        }

        public void setPayType(int payType) {
            this.payType = payType;
        }

        public String getPayTypeStr() {
            return payTypeStr;
        }

        public void setPayTypeStr(String payTypeStr) {
            this.payTypeStr = payTypeStr;
        }

        public double getPresentPrice() {
            return presentPrice;
        }

        public void setPresentPrice(double presentPrice) {
            this.presentPrice = presentPrice;
        }

        public String getPresentPriceStr() {
            return presentPriceStr;
        }

        public void setPresentPriceStr(String presentPriceStr) {
            this.presentPriceStr = presentPriceStr;
        }

        public double getOriginalPrice() {
            return originalPrice;
        }

        public void setOriginalPrice(double originalPrice) {
            this.originalPrice = originalPrice;
        }

        public String getOriginalPriceStr() {
            return originalPriceStr;
        }

        public void setOriginalPriceStr(String originalPriceStr) {
            this.originalPriceStr = originalPriceStr;
        }

        public String getOddChange() {
            return oddChange;
        }

        public void setOddChange(String oddChange) {
            this.oddChange = oddChange;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<CommdityDetailsListBean> getCommdityDetailsList() {
            return commdityDetailsList;
        }

        public void setCommdityDetailsList(List<CommdityDetailsListBean> commdityDetailsList) {
            this.commdityDetailsList = commdityDetailsList;
        }

        public static class CommdityDetailsListBean implements Serializable {
            /**
             * goodName :
             * goodCode :
             * originalUnitPrice : 765.0
             * presentUnitPrice : 0.0
             * count : 2
             */

            private String goodName;
            private String goodCode;
            private double originalUnitPrice;
            private double presentUnitPrice;
            private int count;

            private String originalUnitPriceStr;
            private String presentUnitPriceStr;
            private String id;
            private String presentTotalStr;
            private int goodsType;


            //-------------------------------------
            //自己添加的字段，退款时用

            private int rejectCount = this.count;//退货数量
            private String rejectMoney;//退款金额

            //-------------------------------------


            public int getGoodsType() {
                return goodsType;
            }

            public void setGoodsType(int goodsType) {
                this.goodsType = goodsType;
            }

            public String getPresentTotalStr() {
                return presentTotalStr;
            }

            public void setPresentTotalStr(String presentTotalStr) {
                this.presentTotalStr = presentTotalStr;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getOriginalUnitPriceStr() {
                return originalUnitPriceStr;
            }

            public void setOriginalUnitPriceStr(String originalUnitPriceStr) {
                this.originalUnitPriceStr = originalUnitPriceStr;
            }

            public String getPresentUnitPriceStr() {
                return presentUnitPriceStr;
            }

            public void setPresentUnitPriceStr(String presentUnitPriceStr) {
                this.presentUnitPriceStr = presentUnitPriceStr;
            }

            public int getRejectCount() {
                return rejectCount;
            }

            public void setRejectCount(int rejectCount) {
                this.rejectCount = rejectCount;
            }

            public String getRejectMoney() {
                return rejectMoney;
            }

            public void setRejectMoney(String rejectMoney) {
                this.rejectMoney = rejectMoney;
            }

            public String getGoodName() {
                return goodName;
            }

            public void setGoodName(String goodName) {
                this.goodName = goodName;
            }

            public String getGoodCode() {
                return goodCode;
            }

            public void setGoodCode(String goodCode) {
                this.goodCode = goodCode;
            }

            public double getOriginalUnitPrice() {
                return originalUnitPrice;
            }

            public void setOriginalUnitPrice(double originalUnitPrice) {
                this.originalUnitPrice = originalUnitPrice;
            }

            public double getPresentUnitPrice() {
                return presentUnitPrice;
            }

            public void setPresentUnitPrice(double presentUnitPrice) {
                this.presentUnitPrice = presentUnitPrice;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }
        }

        public class SourceListBean implements Serializable{

            private String preferentialTypeStr;
            private String preferentialPrice;
            private int preferentialDiscountType;
            private int preferentialType;
            private double preferentialDiscountPrice;

            public double getPreferentialDiscountPrice() {
                return preferentialDiscountPrice;
            }

            public void setPreferentialDiscountPrice(double preferentialDiscountPrice) {
                this.preferentialDiscountPrice = preferentialDiscountPrice;
            }

            public int getPreferentialType() {
                return preferentialType;
            }

            public void setPreferentialType(int preferentialType) {
                this.preferentialType = preferentialType;
            }

            public String getPreferentialTypeStr() {
                return preferentialTypeStr;
            }

            public void setPreferentialTypeStr(String preferentialTypeStr) {
                this.preferentialTypeStr = preferentialTypeStr;
            }

            public String getPreferentialPrice() {
                return preferentialPrice;
            }

            public void setPreferentialPrice(String preferentialPrice) {
                this.preferentialPrice = preferentialPrice;
            }

            public int getPreferentialDiscountType() {
                return preferentialDiscountType;
            }

            public void setPreferentialDiscountType(int preferentialDiscountType) {
                this.preferentialDiscountType = preferentialDiscountType;
            }
        }
    }
}
