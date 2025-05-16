package org.openspg.idea.conceptRule.structureView.viewElement;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.lang.psi.*;
import org.openspg.idea.schema.SchemaIcons;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConceptRuleRuleWrapperStructureViewElement extends AbstractConceptRuleStructureViewElement<ConceptRuleRuleWrapper> {

    public ConceptRuleRuleWrapperStructureViewElement(ConceptRuleRuleWrapper element) {
        super(element);
    }

    @Override
    public String getNullableAlphaSortKey() {
        return myElement.getRuleWrapperHead().getRuleWrapperPattern().getText();
    }

    @Override
    protected PresentationData createPresentation(ConceptRuleRuleWrapper element) {
        List<String> labels = new ArrayList<>();
        List<String> locations = new ArrayList<>();

        myElement.getRuleWrapperHead()
                .getRuleWrapperPattern()
                .getLabelExpressionList()
                .stream()
                .flatMap(x -> x.getLabelNameList().stream())
                .flatMap(labelNameElement -> {
                    if (labelNameElement.getEntityType() != null) {
                        return Stream.of(labelNameElement.getEntityType());
                    }
                    return labelNameElement.getConceptNameList().stream();
                })
                .forEach(psiElement -> {
                    if (psiElement instanceof ConceptRuleEntityType entityType) {
                        String label = entityType.getIdentifierList()
                                .stream()
                                .map(ConceptRuleIdentifier::getLabel)
                                .collect(Collectors.joining("."));
                        if (labels.isEmpty()) {
                            labels.add(label);
                        } else {
                            locations.add(label);
                        }

                    } else if (psiElement instanceof ConceptRuleConceptName conceptName) {
                        String label = conceptName.getMetaConceptType()
                                .getIdentifierList()
                                .stream()
                                .map(ConceptRuleIdentifier::getLabel)
                                .collect(Collectors.joining("."));
                        String instanceId = conceptName.getConceptInstanceId().getLabel();
                        if (labels.isEmpty()) {
                            labels.add(label);
                            locations.add(instanceId);
                        } else {
                            locations.add("(" + label + " / " + instanceId + ")");
                        }
                    }
                });

        return new PresentationData(
                String.join(" ", labels),
                String.join(" ", locations),
                SchemaIcons.Nodes.Entity,
                null
        );
    }

    @Override
    public TreeElement @NotNull [] getChildren() {
        ConceptRuleRuleWrapperBody ruleWrapperBodyElement = myElement.getRuleWrapperBody();
        if (ruleWrapperBodyElement == null) {
            return TreeElement.EMPTY_ARRAY;
        }

        List<ConceptRuleTheDefineStructure> elements = PsiTreeUtil.getChildrenOfTypeAsList(ruleWrapperBodyElement, ConceptRuleTheDefineStructure.class);

        List<TreeElement> treeElements = new ArrayList<>(elements.size());
        for (ConceptRuleTheDefineStructure element : elements) {
            treeElements.add(new ConceptRuleTheDefineStructureStructureViewElement(element));
        }

        return treeElements.toArray(new TreeElement[0]);
    }

}
