package psjw.coupon.domain.model.coupon;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import psjw.coupon.common.entity.BaseAuditEntity;
import psjw.coupon.domain.enumtype.coupon.IssueStatus;
import psjw.coupon.domain.enumtype.coupon.IssueType;
import psjw.coupon.domain.model.member.Member;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uq_member_coupon", columnNames = {"member_id", "coupon_id"})
})
public class CouponIssue extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private CouponCode couponCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssueType issueType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssueStatus issueStatus;

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    @Column(nullable = false)
    private Boolean isCanceled;

    @Column(nullable = false)
    private LocalDateTime canceledAt;

}
