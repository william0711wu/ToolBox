package com.duowan.common.sitemesh;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.sitemesh.content.tagrules.html.Sm2TagRuleBundle;

/**
 * 使用自定义的sitemesh filter  ，扩展默认的ConfigurableSiteMeshFilter，并支持sitemesh2的布局模板配置，如下：
 * <content tag="heading"></content>
 *
 * <sitemesh:write property="page.heading"/>
 */
public class CusSitemeshFilter extends ConfigurableSiteMeshFilter {
    @Override
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
        builder.addTagRuleBundle(new Sm2TagRuleBundle());
    }
}
