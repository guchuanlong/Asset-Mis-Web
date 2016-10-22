package com.myunihome.myxapp.web.system.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import com.myunihome.myxapp.common.api.area.interfaces.IGnAreaQuerySV;
import com.myunihome.myxapp.common.api.area.param.AreaLevel;
import com.myunihome.myxapp.common.api.area.param.GnAreaVo;
import com.myunihome.myxapp.common.api.cache.interfaces.ICacheSV;
import com.myunihome.myxapp.framepage.util.OperInfoUtil;
import com.myunihome.myxapp.utils.util.DubboConsumerFactory;

public final class AreaUtil {

	private AreaUtil() {
	}

	/**
	 * 获得城市名称
	 * 
	 * @param tenantId
	 * @param provinceCode
	 * @param cityCode
	 * @return
	 * @author jiaxs
	 * @ApiDocMethod
	 */
	public static String getCityName(String tenantId, String provinceCode, String cityCode) {
		ICacheSV cacheSV = DubboConsumerFactory.getService("getCacheSV");
		String cityName = null;
		if (StringUtils.isNotEmpty(cityCode)) {
			if ("000".equals(cityCode)) {
				if ("00".equals(provinceCode)) {
					cityName = "全国";
				} else {
					cityName = "全省";
				}
			} else {
				cityName = cacheSV.getAreaName(tenantId, cityCode);
				if (cityCode.equals(cityName)) {
					cityName = null;
				}
			}
		}
		return cityName;
	}

	/**
	 * 获得省份名称
	 * 
	 * @param tenantId
	 * @param provinceCode
	 * @return
	 * @author jiaxs
	 * @ApiDocMethod
	 */
	public static String getProvinceName(String tenantId, String provinceCode) {
		String provinceName = null;
		ICacheSV cacheSV = DubboConsumerFactory.getService("getCacheSV");
		if (provinceCode != null) {
			provinceName = cacheSV.getAreaName(tenantId, provinceCode);
			if (provinceCode.equals(provinceName)) {
				provinceName = null;
			}
		}
		return provinceName;
	}

	/**
	 * 获得区域list列表
	 * @param areaLevel
	 * @param parentAreaCode
	 * @param session
	 * @return
	 * @author jiaxs
	 * @ApiDocMethod
	 */
	public static List<GnAreaVo> getAreas(AreaLevel areaLevel, String parentAreaCode, HttpSession session) {
		IGnAreaQuerySV gnAreaQuerySV = DubboConsumerFactory.getService(IGnAreaQuerySV.class);
		String level = areaLevel.getLevelValue();
		List<GnAreaVo> resultList = new ArrayList<GnAreaVo>();
		if (level.equalsIgnoreCase(AreaLevel.PROVINCE_LEVEL.getLevelValue())) {
			resultList = gnAreaQuerySV.getProvinceList();
		} else if (level.equalsIgnoreCase(AreaLevel.CITY_LEVEL.getLevelValue())) {
			resultList = gnAreaQuerySV.getCityListByProviceCode(parentAreaCode);
		} else if (level.equalsIgnoreCase(AreaLevel.COUNTY_LEVEL.getLevelValue())) {
			resultList = gnAreaQuerySV.getCountyListByCityCode(parentAreaCode);
		} else if (level.equalsIgnoreCase(AreaLevel.STREET_LEVEL.getLevelValue())) {
			resultList = gnAreaQuerySV.getStreetListByCountyCode(parentAreaCode);
		} else {
			resultList = gnAreaQuerySV.getAreaListByStreetCode(OperInfoUtil.getTenantId(session), parentAreaCode);
		}

		return resultList;
	}
}
