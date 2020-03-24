package api.systems.addons;

import org.schema.game.common.controller.elements.scanner.ScanAddOn;

public class Scanner {
    private ScanAddOn internalScanner;
    public Scanner(ScanAddOn scan){
        this.internalScanner = scan;
    }
    public float getScanStrength(){
        return internalScanner.getActiveStrength();
    }

    public ScanAddOn getInternalScanner() {
        return internalScanner;
    }
}
