package com.easyjava.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @Description 测试商品表 操作接口类
  */
public interface ProductInfoMapper<T, P> extends BaseMapper<T,  P> {

    /**
     * 根据Id查询
     */
	T selectById(@Param("id") Integer id);

    /**
     * 根据Id更新
     */
	Integer updateById(@Param("id") Integer id);

    /**
     * 根据Id删除
     */
	Integer deleteById(@Param("id") Integer id);

    /**
     * 根据Code查询
     */
	T selectByCode(@Param("code") String code);

    /**
     * 根据Code更新
     */
	Integer updateByCode(@Param("code") String code);

    /**
     * 根据Code删除
     */
	Integer deleteByCode(@Param("code") String code);

    /**
     * 根据SkuType、ColorType查询
     */
	T selectBySkuTypeAndColorType(@Param("skuType") Integer skuType, @Param("colorType") Integer colorType);

    /**
     * 根据SkuType、ColorType更新
     */
	Integer updateBySkuTypeAndColorType(@Param("skuType") Integer skuType, @Param("colorType") Integer colorType);

    /**
     * 根据SkuType、ColorType删除
     */
	Integer deleteBySkuTypeAndColorType(@Param("skuType") Integer skuType, @Param("colorType") Integer colorType);

}