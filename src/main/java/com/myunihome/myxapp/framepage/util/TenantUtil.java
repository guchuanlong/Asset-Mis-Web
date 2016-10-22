package com.myunihome.myxapp.framepage.util;

import com.myunihome.myxapp.base.exception.SystemException;
import com.myunihome.myxapp.common.api.tenant.interfaces.IGnTenantQuerySV;
import com.myunihome.myxapp.common.api.tenant.param.GnTenantConditon;
import com.myunihome.myxapp.common.api.tenant.param.GnTenantVo;
import com.myunihome.myxapp.paas.util.StringUtil;
import com.myunihome.myxapp.utils.util.DubboConsumerFactory;

public final class TenantUtil {

	private TenantUtil() {

	}
    public static GnTenantVo getGnTenant(String tenantId) {
        if (StringUtil.isBlank(tenantId)) {
            throw new SystemException("获取租户信息失败：租户ID不存在");
        }
        IGnTenantQuerySV sv = DubboConsumerFactory.getService(IGnTenantQuerySV.class);
        GnTenantConditon q = new GnTenantConditon();
        q.setTenantId(tenantId);
        GnTenantVo vo = sv.getTenant(q);
        if (vo == null) {
            throw new SystemException("获取租户信息失败：租户[" + tenantId + "]在系统中不存在");
        }
        return vo;
    }
}
