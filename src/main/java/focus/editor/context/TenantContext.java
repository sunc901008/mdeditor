package focus.editor.context;

import org.springframework.stereotype.Component;

/**
 * @author sunc
 * @date 2019/10/25 9:56
 * @description TenantContext
 */

@Component
public class TenantContext {
    private int currentTenantId;

    public int getCurrentTenantId() {
        return currentTenantId;
    }

    public void setCurrentTenantId(int currentTenantId) {
        this.currentTenantId = currentTenantId;
    }
}