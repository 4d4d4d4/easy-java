package com.easyjava.entity.query;

import java.math.BigDecimal;
import java.util.Date;



/**
 * @Description: 测试商品表查询对象
 * @Date: 2024/04/10 22:58
 * @Created by: 憧憬
  */
public class ProductInfoQuery extends BaseQuery {
    /**
     * 自增id
     */
	private Integer id;

    /**
     * 公司id
     */
	private String companyId;

	private String companyIdFuzzy;

    /**
     * 商品编号
     */
	private String code;

	private String codeFuzzy;

    /**
     * 商品名称
     */
	private String productName;

	private String productNameFuzzy;

    /**
     * 价格
     */
	private BigDecimal price;

    /**
     * sku类型
     */
	private Integer skuType;

    /**
     * 颜色类型
     */
	private Integer colorType;

    /**
     * 创建时间
     */
	private Date createTime;

	private String createTimeStart;
	private String createTimeEnd;

    /**
     * 创建日期
     */
	private Date createDate;

	private String createDateStart;
	private String createDateEnd;

    /**
     * 库存
     */
	private Long stock;

    /**
     * 状态
     */
	private Integer status;

	public void setId(Integer id) {
		this.id = id;
	 }

	public Integer getId() {
		return this.id;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	 }

	public String getCompanyId() {
		return this.companyId;
	}

	public void setCode(String code) {
		this.code = code;
	 }

	public String getCode() {
		return this.code;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	 }

	public String getProductName() {
		return this.productName;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	 }

	public BigDecimal getPrice() {
		return this.price;
	}

	public void setSkuType(Integer skuType) {
		this.skuType = skuType;
	 }

	public Integer getSkuType() {
		return this.skuType;
	}

	public void setColorType(Integer colorType) {
		this.colorType = colorType;
	 }

	public Integer getColorType() {
		return this.colorType;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	 }

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	 }

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setStock(Long stock) {
		this.stock = stock;
	 }

	public Long getStock() {
		return this.stock;
	}

	public void setStatus(Integer status) {
		this.status = status;
	 }

	public Integer getStatus() {
		return this.status;
	}

	public void setCompanyIdFuzzy(String companyIdFuzzy) {
		this.companyIdFuzzy = companyIdFuzzy;
	 }

	public String getCompanyIdFuzzy() {
		return this.companyIdFuzzy;
	}

	public void setCodeFuzzy(String codeFuzzy) {
		this.codeFuzzy = codeFuzzy;
	 }

	public String getCodeFuzzy() {
		return this.codeFuzzy;
	}

	public void setProductNameFuzzy(String productNameFuzzy) {
		this.productNameFuzzy = productNameFuzzy;
	 }

	public String getProductNameFuzzy() {
		return this.productNameFuzzy;
	}

	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart = createTimeStart;
	 }

	public String getCreateTimeStart() {
		return this.createTimeStart;
	}

	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	 }

	public String getCreateTimeEnd() {
		return this.createTimeEnd;
	}

	public void setCreateDateStart(String createDateStart) {
		this.createDateStart = createDateStart;
	 }

	public String getCreateDateStart() {
		return this.createDateStart;
	}

	public void setCreateDateEnd(String createDateEnd) {
		this.createDateEnd = createDateEnd;
	 }

	public String getCreateDateEnd() {
		return this.createDateEnd;
	}

}