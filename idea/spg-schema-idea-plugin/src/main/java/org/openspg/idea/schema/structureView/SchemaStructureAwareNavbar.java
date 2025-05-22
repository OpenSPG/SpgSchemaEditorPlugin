package org.openspg.idea.schema.structureView;

import com.intellij.ide.navigationToolbar.StructureAwareNavBarModelExtension;
import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openspg.idea.schema.SchemaLanguage;
import org.openspg.idea.schema.psi.SchemaFile;

import javax.swing.*;

public class SchemaStructureAwareNavbar extends StructureAwareNavBarModelExtension {

    @NotNull
    @Override
    protected Language getLanguage() {
        return SchemaLanguage.INSTANCE;
    }

    @Override
    public @Nullable String getPresentableText(Object object) {
        if (object instanceof SchemaFile) {
            return ((SchemaFile) object).getName();
        }

//        if (object instanceof SchemaEntityInfo) {
//            return ((SchemaEntityInfo) object).getEntityName();
//        }

        return null;
    }

    @Override
    @Nullable
    public Icon getIcon(Object object) {
//        if (object instanceof SchemaEntityInfo) {
//            return SchemaIcons.Nodes.Entity;
//        }

        return null;
    }

}
