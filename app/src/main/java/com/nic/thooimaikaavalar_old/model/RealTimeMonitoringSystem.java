package com.nic.thooimaikaavalar_old.model;

import android.graphics.Bitmap;

/**
 * Created by AchanthiSundar on 01-11-2017.
 */

public class RealTimeMonitoringSystem {

    private String distictCode;
    private String districtName;

    private String blockCode;
    private String SchemeGroupName;
    private String SchemeName;
    private String Description;
    private String Latitude;
    private String selectedBlockCode;

    private String FinancialYear;
    private String AgencyName;
    private String WorkGroupNmae;
    private String WorkName;
    private String PvCode;
    private String PvName;

    private String blockName;
    private String Gender;
    private Integer CurrentStage;

    private String Name;
    private String BeneficiaryName;
    private String workGroupID;
    private String workTypeID;

    ////////////////////////// Need
    public  String KEY_ID;
    public  String KEY_capacity_of_mcc_name;
    public  String KEY_thooimai_kaavalars_name;
    public  String KEY_photographs_name;
    public  String KEY_water_supply_availability_name;
    private String HabCode;
    private String HabitationName;
    private String HabitationNameTa;

    public String getKEY_ID() {
        return KEY_ID;
    }

    public void setKEY_ID(String KEY_ID) {
        this.KEY_ID = KEY_ID;
    }

    public String getKEY_capacity_of_mcc_name() {
        return KEY_capacity_of_mcc_name;
    }

    public void setKEY_capacity_of_mcc_name(String KEY_capacity_of_mcc_name) {
        this.KEY_capacity_of_mcc_name = KEY_capacity_of_mcc_name;
    }

    public String getKEY_thooimai_kaavalars_name() {
        return KEY_thooimai_kaavalars_name;
    }

    public void setKEY_thooimai_kaavalars_name(String KEY_thooimai_kaavalars_name) {
        this.KEY_thooimai_kaavalars_name = KEY_thooimai_kaavalars_name;
    }

    public String getKEY_photographs_name() {
        return KEY_photographs_name;
    }

    public void setKEY_photographs_name(String KEY_photographs_name) {
        this.KEY_photographs_name = KEY_photographs_name;
    }

    public String getKEY_water_supply_availability_name() {
        return KEY_water_supply_availability_name;
    }

    public void setKEY_water_supply_availability_name(String KEY_water_supply_availability_name) {
        this.KEY_water_supply_availability_name = KEY_water_supply_availability_name;
    }

    public String getHabCode() {
        return HabCode;
    }

    public void setHabCode(String habCode) {
        HabCode = habCode;
    }

    public String getHabitationName() {
        return HabitationName;
    }

    public void setHabitationName(String habitationName) {
        HabitationName = habitationName;
    }

    public String getHabitationNameTa() {
        return HabitationNameTa;
    }

    public void setHabitationNameTa(String habitationNameTa) {
        HabitationNameTa = habitationNameTa;
    }
    /////////////////////////

    /////////////////////// Basic Details ////////////////
    String mcc_name;
    String capacity_of_mcc_id;
    String water_supply_availability_id;
    String water_supply_availability_other;
    String date_of_commencement;
    String center_image;
    String center_image_latitude;
    String center_image_longtitude;
    String mcc_id;
    String capacity_of_mcc_name;
    String composting_center_image_available;
    String no_of_thooimai_kaavalars;
    String water_supply_availability_name;

    String name_of_the_thooimai_kaavalars;
    String mobile_no;
    String date_of_engagement;
    String date_of_training_given;

    String  thooimai_kaavalar_id;
    String  component_id;
    String  photograph_remark;
    String  photograph_serial_no;
    Bitmap  component_image;
    String  component_image_string;

    String  households_waste;
    String  shops_waste;
    String  market_waste;
    String  hotels_waste;
    String  others_waste;
    String  tot_bio_waste_collected;
    String  tot_bio_waste_shredded;
    String  tot_bio_compost_produced;
    String  tot_bio_compost_sold;
    String  date_of_save;

    public String getHouseholds_waste() {
        return households_waste;
    }

    public void setHouseholds_waste(String households_waste) {
        this.households_waste = households_waste;
    }

    public String getShops_waste() {
        return shops_waste;
    }

    public void setShops_waste(String shops_waste) {
        this.shops_waste = shops_waste;
    }

    public String getMarket_waste() {
        return market_waste;
    }

    public void setMarket_waste(String market_waste) {
        this.market_waste = market_waste;
    }

    public String getHotels_waste() {
        return hotels_waste;
    }

    public void setHotels_waste(String hotels_waste) {
        this.hotels_waste = hotels_waste;
    }

    public String getOthers_waste() {
        return others_waste;
    }

    public void setOthers_waste(String others_waste) {
        this.others_waste = others_waste;
    }

    public String getTot_bio_waste_collected() {
        return tot_bio_waste_collected;
    }

    public void setTot_bio_waste_collected(String tot_bio_waste_collected) {
        this.tot_bio_waste_collected = tot_bio_waste_collected;
    }

    public String getTot_bio_waste_shredded() {
        return tot_bio_waste_shredded;
    }

    public void setTot_bio_waste_shredded(String tot_bio_waste_shredded) {
        this.tot_bio_waste_shredded = tot_bio_waste_shredded;
    }

    public String getTot_bio_compost_produced() {
        return tot_bio_compost_produced;
    }

    public void setTot_bio_compost_produced(String tot_bio_compost_produced) {
        this.tot_bio_compost_produced = tot_bio_compost_produced;
    }

    public String getTot_bio_compost_sold() {
        return tot_bio_compost_sold;
    }

    public void setTot_bio_compost_sold(String tot_bio_compost_sold) {
        this.tot_bio_compost_sold = tot_bio_compost_sold;
    }

    public String getDate_of_save() {
        return date_of_save;
    }

    public void setDate_of_save(String date_of_save) {
        this.date_of_save = date_of_save;
    }

    public String getPhotograph_serial_no() {
        return photograph_serial_no;
    }

    public void setPhotograph_serial_no(String photograph_serial_no) {
        this.photograph_serial_no = photograph_serial_no;
    }

    public String getComponent_image_string() {
        return component_image_string;
    }

    public void setComponent_image_string(String component_image_string) {
        this.component_image_string = component_image_string;
    }

    public String getComponent_id() {
        return component_id;
    }

    public void setComponent_id(String component_id) {
        this.component_id = component_id;
    }

    public String getPhotograph_remark() {
        return photograph_remark;
    }

    public void setPhotograph_remark(String photograph_remark) {
        this.photograph_remark = photograph_remark;
    }

    public Bitmap getComponent_image() {
        return component_image;
    }

    public void setComponent_image(Bitmap component_image) {
        this.component_image = component_image;
    }

    public String getThooimai_kaavalar_id() {
        return thooimai_kaavalar_id;
    }

    public void setThooimai_kaavalar_id(String thooimai_kaavalar_id) {
        this.thooimai_kaavalar_id = thooimai_kaavalar_id;
    }

    public String getDate_of_training_given() {
        return date_of_training_given;
    }

    public void setDate_of_training_given(String date_of_training_given) {
        this.date_of_training_given = date_of_training_given;
    }

    public String getDate_of_engagement() {
        return date_of_engagement;
    }

    public void setDate_of_engagement(String date_of_engagement) {
        this.date_of_engagement = date_of_engagement;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getName_of_the_thooimai_kaavalars() {
        return name_of_the_thooimai_kaavalars;
    }

    public void setName_of_the_thooimai_kaavalars(String name_of_the_thooimai_kaavalars) {
        this.name_of_the_thooimai_kaavalars = name_of_the_thooimai_kaavalars;
    }

    public String getMcc_id() {
        return mcc_id;
    }

    public void setMcc_id(String mcc_id) {
        this.mcc_id = mcc_id;
    }

    public String getCapacity_of_mcc_name() {
        return capacity_of_mcc_name;
    }

    public void setCapacity_of_mcc_name(String capacity_of_mcc_name) {
        this.capacity_of_mcc_name = capacity_of_mcc_name;
    }

    public String getComposting_center_image_available() {
        return composting_center_image_available;
    }

    public void setComposting_center_image_available(String composting_center_image_available) {
        this.composting_center_image_available = composting_center_image_available;
    }

    public String getNo_of_thooimai_kaavalars() {
        return no_of_thooimai_kaavalars;
    }

    public void setNo_of_thooimai_kaavalars(String no_of_thooimai_kaavalars) {
        this.no_of_thooimai_kaavalars = no_of_thooimai_kaavalars;
    }

    public String getWater_supply_availability_name() {
        return water_supply_availability_name;
    }

    public void setWater_supply_availability_name(String water_supply_availability_name) {
        this.water_supply_availability_name = water_supply_availability_name;
    }

    public String getCenter_image_latitude() {
        return center_image_latitude;
    }

    public void setCenter_image_latitude(String center_image_latitude) {
        this.center_image_latitude = center_image_latitude;
    }

    public String getCenter_image_longtitude() {
        return center_image_longtitude;
    }

    public void setCenter_image_longtitude(String center_image_longtitude) {
        this.center_image_longtitude = center_image_longtitude;
    }

    public String getMcc_name() {
        return mcc_name;
    }

    public void setMcc_name(String mcc_name) {
        this.mcc_name = mcc_name;
    }

    public String getCapacity_of_mcc_id() {
        return capacity_of_mcc_id;
    }

    public void setCapacity_of_mcc_id(String capacity_of_mcc_id) {
        this.capacity_of_mcc_id = capacity_of_mcc_id;
    }

    public String getWater_supply_availability_id() {
        return water_supply_availability_id;
    }

    public void setWater_supply_availability_id(String water_supply_availability_id) {
        this.water_supply_availability_id = water_supply_availability_id;
    }

    public String getWater_supply_availability_other() {
        return water_supply_availability_other;
    }

    public void setWater_supply_availability_other(String water_supply_availability_other) {
        this.water_supply_availability_other = water_supply_availability_other;
    }

    public String getDate_of_commencement() {
        return date_of_commencement;
    }

    public void setDate_of_commencement(String date_of_commencement) {
        this.date_of_commencement = date_of_commencement;
    }

    public void setCenter_image(String center_image) {
        this.center_image = center_image;
    }
    public String getCenter_image(){
        return center_image;
    }

    // ///////////////////

    public String getBeneficiaryFatherName() {
        return BeneficiaryFatherName;
    }

    public void setBeneficiaryFatherName(String beneficiaryFatherName) {
        BeneficiaryFatherName = beneficiaryFatherName;
    }

    private String BeneficiaryFatherName;
    private String Community;
    private String IntialAmount;

    private String AmountSpendSoFar;
    private String LastVisitedDate;

    private Integer pendingActivity;
    public String workStageName;
    private String workStageCode;
    private String workStageOrder;
    private Integer schemeSequentialID;

    private Integer schemeGroupID;
    private Integer schemeID;
    private Integer asValue;
    private String stageName;
    private String workTypeName;
    private String ynCompleted;
    private String cdProtWorkYn;
    private Integer stateCode;

    private String roadName;
    private Integer cdWorkNo;
    private Integer cdCode;
    private String cdName;
    private String chainageMeter;
    private Integer cdTypeId;

    public Integer getCdTypeId() {
        return cdTypeId;
    }

    public void setCdTypeId(Integer cdTypeId) {
        this.cdTypeId = cdTypeId;
    }

    public String getWorkTypeFlagLe() {
        return workTypeFlagLe;
    }

    public void setWorkTypeFlagLe(String workTypeFlagLe) {
        this.workTypeFlagLe = workTypeFlagLe;
    }

    private String workTypeFlagLe;

    public String getChainageMeter() {
        return chainageMeter;
    }

    public void setChainageMeter(String chainageMeter) {
        this.chainageMeter = chainageMeter;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public Integer getCdWorkNo() {
        return cdWorkNo;
    }

    public void setCdWorkNo(Integer cdWorkNo) {
        this.cdWorkNo = cdWorkNo;
    }

    public Integer getCdCode() {
        return cdCode;
    }

    public void setCdCode(Integer cdCode) {
        this.cdCode = cdCode;
    }

    public String getCdName() {
        return cdName;
    }

    public void setCdName(String cdName) {
        this.cdName = cdName;
    }

    public String getYnCompleted() {
        return ynCompleted;
    }

    public void setYnCompleted(String ynCompleted) {
        this.ynCompleted = ynCompleted;
    }

    public String getCdProtWorkYn() {
        return cdProtWorkYn;
    }

    public void setCdProtWorkYn(String cdProtWorkYn) {
        this.cdProtWorkYn = cdProtWorkYn;
    }

    public Integer getStateCode() {
        return stateCode;
    }

    public void setStateCode(Integer stateCode) {
        this.stateCode = stateCode;
    }

    public String getWorkTypeName() {
        return workTypeName;
    }

    public void setWorkTypeName(String workTypeName) {
        this.workTypeName = workTypeName;
    }

    public Integer getCurrentStage() {
        return CurrentStage;
    }

    public void setCurrentStage(Integer currentStage) {
        CurrentStage = currentStage;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public Integer getAsValue() {
        return asValue;
    }

    public void setAsValue(Integer asValue) {
        this.asValue = asValue;
    }

    public Integer getSchemeID() {
        return schemeID;
    }

    public void setSchemeID(Integer schemeID) {
        this.schemeID = schemeID;
    }

    public Integer getSchemeGroupID() {
        return schemeGroupID;
    }

    public void setSchemeGroupID(Integer schemeGroupID) {
        this.schemeGroupID = schemeGroupID;
    }

    public Integer getSchemeSequentialID() {
        return schemeSequentialID;
    }

    public void setSchemeSequentialID(Integer schemeSequentialID) {
        this.schemeSequentialID = schemeSequentialID;
    }

    private Integer noOfPhotos;

    public String getSchemeGroupName() {
        return SchemeGroupName;
    }

    public void setSchemeGroupName(String schemeGroupName) {
        SchemeGroupName = schemeGroupName;
    }

    public String getSchemeName() {
        return SchemeName;
    }

    public void setSchemeName(String schemeName) {
        SchemeName = schemeName;
    }

    public String getFinancialYear() {
        return FinancialYear;
    }

    public void setFinancialYear(String financialYear) {
        FinancialYear = financialYear;
    }

    public String getAgencyName() {
        return AgencyName;
    }

    public void setAgencyName(String agencyName) {
        AgencyName = agencyName;
    }

    public String getWorkGroupNmae() {
        return WorkGroupNmae;
    }

    public void setWorkGroupNmae(String workGroupNmae) {
        WorkGroupNmae = workGroupNmae;
    }

    public String getWorkName() {
        return WorkName;
    }

    public void setWorkName(String workName) {
        WorkName = workName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getBeneficiaryName() {
        return BeneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        BeneficiaryName = beneficiaryName;
    }

    public String getCommunity() {
        return Community;
    }

    public void setCommunity(String community) {
        Community = community;
    }

    public String getIntialAmount() {
        return IntialAmount;
    }

    public void setIntialAmount(String intialAmount) {
        IntialAmount = intialAmount;
    }

    public String getAmountSpendSoFar() {
        return AmountSpendSoFar;
    }

    public void setAmountSpendSoFar(String amountSpendSoFar) {
        AmountSpendSoFar = amountSpendSoFar;
    }

    public String getLastVisitedDate() {
        return LastVisitedDate;
    }

    public void setLastVisitedDate(String lastVisitedDate) {
        LastVisitedDate = lastVisitedDate;
    }

    public Integer getWorkId() {
        return WorkId;
    }

    public void setWorkId(Integer workId) {
        WorkId = workId;
    }

    private Integer WorkId;
    private String typeOfWork;
    private String imageRemark;
    private String dateTime;
    private String imageAvailable;
    private String createdDate;
    private String workTypeCode;
    private String length;
    private String width;
    private String height;
    private String additional_data_status;
    private String length_water_storage;
    private String observation;
    private String no_trenches_per_worksite;
    private String no_units;
    private String breadth;
    private String depth;
    private String no_units_per_waterbody;
    private String area_covered;
    private String no_plantation_per_site;
    private String structure_sump_available_status;
    private String individual_community;
    private String length_channel;


    public String getLength_water_storage() {
        return length_water_storage;
    }

    public RealTimeMonitoringSystem setLength_water_storage(String length_water_storage) {
        this.length_water_storage = length_water_storage;
        return this;
    }

    public String getObservation() {
        return observation;
    }

    public RealTimeMonitoringSystem setObservation(String observation) {
        this.observation = observation;
        return this;
    }

    public String getNo_trenches_per_worksite() {
        return no_trenches_per_worksite;
    }

    public RealTimeMonitoringSystem setNo_trenches_per_worksite(String no_trenches_per_worksite) {
        this.no_trenches_per_worksite = no_trenches_per_worksite;
        return this;
    }

    public String getNo_units() {
        return no_units;
    }

    public RealTimeMonitoringSystem setNo_units(String no_units) {
        this.no_units = no_units;
        return this;
    }

    public String getBreadth() {
        return breadth;
    }

    public RealTimeMonitoringSystem setBreadth(String breadth) {
        this.breadth = breadth;
        return this;
    }

    public String getDepth() {
        return depth;
    }

    public RealTimeMonitoringSystem setDepth(String depth) {
        this.depth = depth;
        return this;
    }

    public String getNo_units_per_waterbody() {
        return no_units_per_waterbody;
    }

    public RealTimeMonitoringSystem setNo_units_per_waterbody(String no_units_per_waterbody) {
        this.no_units_per_waterbody = no_units_per_waterbody;
        return this;
    }

    public String getArea_covered() {
        return area_covered;
    }

    public RealTimeMonitoringSystem setArea_covered(String area_covered) {
        this.area_covered = area_covered;
        return this;
    }

    public String getNo_plantation_per_site() {
        return no_plantation_per_site;
    }

    public RealTimeMonitoringSystem setNo_plantation_per_site(String no_plantation_per_site) {
        this.no_plantation_per_site = no_plantation_per_site;
        return this;
    }

    public String getStructure_sump_available_status() {
        return structure_sump_available_status;
    }

    public RealTimeMonitoringSystem setStructure_sump_available_status(String structure_sump_available_status) {
        this.structure_sump_available_status = structure_sump_available_status;
        return this;
    }

    public String getIndividual_community() {
        return individual_community;
    }

    public RealTimeMonitoringSystem setIndividual_community(String individual_community) {
        this.individual_community = individual_community;
        return this;
    }

    public String getLength_channel() {
        return length_channel;
    }

    public RealTimeMonitoringSystem setLength_channel(String length_channel) {
        this.length_channel = length_channel;
        return this;
    }

    public String getAdditional_data_status() {
        return additional_data_status;
    }

    public RealTimeMonitoringSystem setAdditional_data_status(String additional_data_status) {
        this.additional_data_status = additional_data_status;
        return this;
    }

    public String getLength() {
        return length;
    }

    public RealTimeMonitoringSystem setLength(String length) {
        this.length = length;
        return this;
    }

    public String getWidth() {
        return width;
    }

    public RealTimeMonitoringSystem setWidth(String width) {
        this.width = width;
        return this;
    }

    public String getHeight() {
        return height;
    }

    public RealTimeMonitoringSystem setHeight(String height) {
        this.height = height;
        return this;
    }

    public RealTimeMonitoringSystem setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getWorkTypeCode() {
        return workTypeCode;
    }

    public void setWorkTypeCode(String workTypeCode) {
        this.workTypeCode = workTypeCode;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getTypeOfWork() {
        return typeOfWork;
    }

    public void setTypeOfWork(String typeOfWork) {
        this.typeOfWork = typeOfWork;
    }

    public String getImageAvailable() {
        return imageAvailable;
    }

    public void setImageAvailable(String imageAvailable) {
        this.imageAvailable = imageAvailable;
    }

    public String getImageRemark() {
        return imageRemark;
    }

    public void setImageRemark(String imageRemark) {
        this.imageRemark = imageRemark;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }



    public Integer getNoOfPhotos() {
        return noOfPhotos;
    }

    public void setNoOfPhotos(Integer noOfPhotos) {
        this.noOfPhotos = noOfPhotos;
    }


    public Integer getPendingActivity() {
        return pendingActivity;
    }

    public void setPendingActivity(Integer pendingActivity) {
        this.pendingActivity = pendingActivity;
    }




    public String getPvName() {
        return PvName;
    }

    public void setPvName(String pvName) {
        PvName = pvName;
    }



    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


    public String getDistictCode() {
        return distictCode;
    }

    public void setDistictCode(String distictCode) {
        this.distictCode = distictCode;
    }

    public String getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(String blockCode) {
        this.blockCode = blockCode;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getSelectedBlockCode() {
        return selectedBlockCode;
    }

    public void setSelectedBlockCode(String selectedBlockCode) {
        this.selectedBlockCode = selectedBlockCode;
    }




    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    private String Longitude;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Bitmap getImage() {
        return Image;
    }

    public void setImage(Bitmap image) {
        Image = image;
    }

    private Bitmap Image;



    public String getPvCode() {
        return PvCode;
    }

    public void setPvCode(String pvCode) {
        this.PvCode = pvCode;
    }

    public void setWorkStageName(String workStageName) {
        this.workStageName = workStageName;
    }

    public String getWorkStageName() {
        return workStageName;
    }

    public void setWorkGroupID(String workGroupID) {
        this.workGroupID = workGroupID;
    }

    public String getWorkGroupID() {
        return workGroupID;
    }

    public void setWorkTypeID(String workTypeID) {
        this.workTypeID = workTypeID;
    }

    public String getWorkTypeID() {
        return workTypeID;
    }

    public void setWorkStageCode(String workStageCode) {
        this.workStageCode = workStageCode;
    }

    public String getWorkStageCode() {
        return workStageCode;
    }

    public void setWorkStageOrder(String workStageOrder) {
        this.workStageOrder = workStageOrder;
    }

    public String getWorkStageOrder() {
        return workStageOrder;
    }

    public Integer getId() {
        return id;
    }
    private Integer id;

}