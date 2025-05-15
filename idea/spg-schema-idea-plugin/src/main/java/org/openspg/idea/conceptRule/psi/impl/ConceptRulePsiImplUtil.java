package org.openspg.idea.conceptRule.psi.impl;

import org.apache.commons.lang3.StringUtils;
import org.openspg.idea.lang.psi.ConceptRuleNamespace;
import org.openspg.idea.lang.psi.ConceptRuleNodePattern;
import org.openspg.idea.lang.psi.ConceptRulePredicatedDefine;
import org.openspg.idea.lang.psi.ConceptRuleRuleWrapper;

import java.util.List;

public class ConceptRulePsiImplUtil {

    // ============================================
    // ConceptRuleNamespace methods
    //
    public static String getValue(ConceptRuleNamespace element) {
        return element.getNamespaceValue().getText();
    }

    // ============================================
    // ConceptRuleRuleWrapper methods
    //
    public static String getLabel(ConceptRuleRuleWrapper element) {
        return element.getRuleWrapperHead().getRuleWrapperTitle().getText();
    }

    // ============================================
    // ConceptRulePredicatedDefine methods
    //
    public static String getMajorLabel(ConceptRulePredicatedDefine element) {
        List<ConceptRuleNodePattern> nodePatterns = element.getNodePatternList();
        if (nodePatterns.isEmpty()) {
            return StringUtils.EMPTY;
        }
        return nodePatterns.get(0).getText();
    }

    public static String getMinorLabel(ConceptRulePredicatedDefine element) {
        List<ConceptRuleNodePattern> nodePatterns = element.getNodePatternList();
        if (nodePatterns.size() <= 1) {
            return StringUtils.EMPTY;
        }
        return element.getFullEdgePointingRight().getText().trim() + nodePatterns.get(1).getText();
    }

}
