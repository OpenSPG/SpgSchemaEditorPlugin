package org.openspg.idea.schema.util;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.grammar.psi.SchemaTypes;
import org.openspg.idea.schema.lang.psi.SchemaEntity;
import org.openspg.idea.schema.lang.psi.SchemaEntityHead;
import org.openspg.idea.schema.lang.psi.SchemaNamespace;
import org.openspg.idea.schema.lang.psi.SchemaPlainTextBlock;

import java.util.*;
import java.util.stream.Collectors;

public class PsiUtils {

    public static @NotNull List<PsiElement> searchChildrenOfAnyType(
            @NotNull PsiElement psiRoot,
            boolean recursion,
            IElementType @NotNull ... types
    ) {
        List<PsiElement> results = new ArrayList<>();
        searchChildrenOfAnyType(results, psiRoot, recursion, types);
        return results;
    }

    private static void searchChildrenOfAnyType(
            @NotNull List<PsiElement> result,
            @NotNull PsiElement psiRoot,
            boolean recursion,
            IElementType @NotNull ... types
    ) {
        PsiElement psiChild = psiRoot.getFirstChild();
        while (psiChild != null) {
            IElementType childType = psiChild.getNode().getElementType();
            for (IElementType type : types) {
                if (childType.equals(type)) {
                    result.add(psiChild);
                    break;
                }
            }

            if (recursion) {
                searchChildrenOfAnyType(result, psiChild, recursion, types);
            }
            psiChild = psiChild.getNextSibling();
        }
    }

    public static String readPlainTextAfterNode(
            @NotNull PsiElement element,
            @NotNull IElementType nodeType
    ) {
        ASTNode node = element.getNode().findChildByType(nodeType);
        if (node == null) {
            return null;
        }

        StringBuilder result = new StringBuilder();

        PsiElement psiElement = node.getPsi().getNextSibling();
        while (psiElement != null) {
            if (psiElement instanceof SchemaPlainTextBlock
                    || psiElement.getNode().getElementType() != SchemaTypes.COMMENT) {
                result.append(psiElement.getText());
            }
            psiElement = psiElement.getNextSibling();
        }
        return result.toString();
    }

    public static SchemaEntity findEntityByName(@NotNull PsiFile file, @NotNull String entityName) {
        Collection<SchemaEntity> entities = PsiTreeUtil.findChildrenOfType(file, SchemaEntity.class);
        for (SchemaEntity entity : entities) {
            SchemaEntityHead head = entity.getEntityHead();
            if (entityName.equals(head.getBasicStructureDeclaration().getStructureNameDeclaration().getText())) {
                return entity;
            }
        }
        return null;
    }

    public static Map<String, Object> toJson(@NotNull PsiFile file) {
        Map<String, Object> result = new LinkedHashMap<>();

        SchemaNamespace namespace = PsiTreeUtil.getChildOfType(file, SchemaNamespace.class);
        if (namespace != null) {
            result.put("namespace", namespace.toJson());
        }

        result.put("entities", PsiTreeUtil.getChildrenOfTypeAsList(file, SchemaEntity.class)
                .stream()
                .map(SchemaEntity::toJson)
                .collect(Collectors.toList()));
        return result;
    }

}
