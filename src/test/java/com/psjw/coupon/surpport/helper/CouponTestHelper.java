package com.psjw.coupon.surpport.helper;

import com.psjw.coupon.domain.enums.coupon.JobStatus;
import com.psjw.coupon.domain.model.coupon.CouponBatchJob;
import org.springframework.test.util.ReflectionTestUtils;

public class CouponTestHelper {
    public static void setJobStatus(CouponBatchJob job, JobStatus status) {
        ReflectionTestUtils.setField(job, "jobStatus", status);
    }
}
