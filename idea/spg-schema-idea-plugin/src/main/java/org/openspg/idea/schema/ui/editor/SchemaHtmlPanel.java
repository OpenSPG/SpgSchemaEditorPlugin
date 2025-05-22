package org.openspg.idea.schema.ui.editor;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.ui.jcef.JCEFHtmlPanel;
import com.intellij.util.Url;
import com.intellij.util.Urls;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.*;
import org.cef.misc.BoolRef;
import org.cef.network.CefRequest;
import org.jetbrains.ide.BuiltInServerManager;
import org.openspg.idea.schema.lang.psi.SchemaEntity;
import org.openspg.idea.schema.ui.editor.jcef.FetchSchemaApiSupplier;
import org.openspg.idea.schema.ui.editor.jcef.FetchThemeCssSupplier;
import org.openspg.idea.schema.ui.editor.jcef.FocusEntityApiSupplier;
import org.openspg.idea.schema.ui.editor.jcef.SchemaResourceRequestHandler;
import org.openspg.idea.schema.ui.editor.server.PreviewStaticServer;
import org.openspg.idea.schema.ui.editor.server.ResourcesController;
import org.openspg.idea.schema.util.EditorUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class SchemaHtmlPanel extends JCEFHtmlPanel {

    private static final Logger logger = Logger.getInstance(SchemaHtmlPanel.class);

    private final Url HOME_URL = BuiltInServerManager.getInstance().addAuthToken(
            Objects.requireNonNull(
                    Urls.parseEncoded("http://localhost:" + BuiltInServerManager.getInstance().getPort()
                            + PreviewStaticServer.SERVLET_CONTEXT_PATH
                            + ResourcesController.SERVLET_PATH
                            + "/"))
    );

    private final Project myProject;
    private final VirtualFile myFile;
    private byte[] resourceBytes = new byte[0];

    private CefRequestHandler requestHandler;
    private CefLifeSpanHandler lifeSpanHandler;
    private CefDisplayHandler displayHandler;

    public SchemaHtmlPanel(Project project, VirtualFile file) {
        super(null);
        this.myProject = project;
        this.myFile = file;
        initialize();
    }

    private void initialize() {
        loadResourceFile();

        getJBCefClient().addRequestHandler(requestHandler = new CefRequestHandlerAdapter() {
            @Override
            public CefResourceRequestHandler getResourceRequestHandler(CefBrowser browser, CefFrame frame, CefRequest request, boolean isNavigation, boolean isDownload, String requestInitiator, BoolRef disableDefaultHandling) {
                return new SchemaResourceRequestHandler(
                        new FetchSchemaApiSupplier(() -> resourceBytes),
                        new FetchThemeCssSupplier(),
                        new FocusEntityApiSupplier(SchemaHtmlPanel.this::handleEntityActivated)
                );
            }
        }, getCefBrowser());

        getJBCefClient().addLifeSpanHandler(lifeSpanHandler = new CefLifeSpanHandlerAdapter() {
            @Override
            public boolean onBeforePopup(CefBrowser browser, CefFrame frame, String target_url, String target_frame_name) {
                return true;
            }
        }, getCefBrowser());

        getJBCefClient().addDisplayHandler(displayHandler = new CefDisplayHandlerAdapter() {
            @Override
            public boolean onConsoleMessage(CefBrowser browser, CefSettings.LogSeverity level, String message, String source, int line) {
                logger.warn("Console: " + message);
                return true;
            }
        }, getCefBrowser());

        this.loadURL(HOME_URL.toString());
    }

    private void loadResourceFile() {
        PsiFile psiFile = PsiManager.getInstance(myProject).findFile(myFile);
        if (psiFile == null) {
            logger.error("Unparsed file: " + myFile.getName());
            this.resourceBytes = new byte[0];

        } else {
            String schemaString = EditorUtils.fileToJsonString(psiFile);
            logger.warn(schemaString.substring(0, Math.min(160, schemaString.length())));
            String dataString = "{\"code\":0,\"message\":\"success\",\"data\":" + schemaString + "}";
            this.resourceBytes = dataString.getBytes(StandardCharsets.UTF_8);
        }
    }


    @Override
    public void dispose() {
        getJBCefClient().removeRequestHandler(requestHandler, getCefBrowser());
        getJBCefClient().removeLifeSpanHandler(lifeSpanHandler, getCefBrowser());
        getJBCefClient().removeDisplayHandler(displayHandler, getCefBrowser());
        super.dispose();
    }

    public void updateSchema() {
        loadResourceFile();
        String script = "document.getElementById('schema-diagram-refresh-button').click();";
        getCefBrowser().executeJavaScript(script, getCefBrowser().getURL(), 0);
    }

    public void activateEntity(String name) {
        String base64Name = Base64.getEncoder().encodeToString(name.getBytes(StandardCharsets.UTF_8));
        String script = "window.activeEntityName='" + base64Name + "';\n"
                + "document.getElementById('schema-diagram-active-entity-button').click();";
        getCefBrowser().executeJavaScript(script, getCefBrowser().getURL(), 0);
    }

    private Boolean handleEntityActivated(List<SchemaEntity> entities) {
        for (SchemaEntity entity : entities) {
            System.out.println(entity.getEntityHead().getBasicStructureDeclaration().getStructureNameDeclaration().getText());
        }
        return true;
    }

    public void updateStyle() {
        String script = "document.getElementById('schema-diagram-refresh-css-button').click();";
        getCefBrowser().executeJavaScript(script, getCefBrowser().getURL(), 0);
    }
}
