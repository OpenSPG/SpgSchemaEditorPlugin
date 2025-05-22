package org.openspg.idea.conceptRule.psi.impl;

import org.apache.commons.lang3.StringUtils;
import org.openspg.idea.lang.psi.*;

import java.util.List;

public class ConceptRulePsiImplUtil {

    // ============================================
    // ConceptRuleNamespace methods
    //
    public static String getValue(ConceptRuleNamespace element) {
        return element.getNamespaceValue().getText();
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

    // ============================================
    // ConceptRuleIdentifier methods
    //
    public static String getLabel(ConceptRuleIdentifier element) {
        String label = element.getText();
        label = StringUtils.unwrap(label, "`");
        label = StringUtils.unwrap(label, "'");
        label = StringUtils.unwrap(label, "\"");
        return label;
    }

    // ============================================
    // ConceptRuleIdentifier methods
    //
    public static String getLabel(ConceptRuleConceptInstanceId element) {
        String label = element.getText();
        label = StringUtils.unwrap(label, "`");
        label = StringUtils.unwrap(label, "'");
        label = StringUtils.unwrap(label, "\"");
        return label;
    }

}
