package com.mycompany.plugins.blueprint;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import org.json.JSONException;
@CapacitorPlugin(name = "NrccBluePrint")
public class NrccBluePrintPlugin extends Plugin {

    private NrccBluePrint implementation = new NrccBluePrint();

    @PluginMethod
    public void echo(PluginCall call) {
       
        String value = call.getString("value");
        JSObject ret = new JSObject();
        ret.put("value", implementation.echo(value));
        call.resolve(ret);
    }

    @PluginMethod
    public void list(PluginCall call) throws JSONException {
        try {
            JSObject ret = new JSObject();
            ret.put("devices", implementation.list(getContext()));
            call.resolve(ret);
        } catch (RuntimeException e) {
            call.reject(e.getMessage(), e);
        }
    }

    @PluginMethod
    public void connect(PluginCall call) {
        try {
            String address = call.getString("address");
            implementation.connect(getContext(), address);
            call.resolve();
        } catch (RuntimeException e) {
            call.reject(e.getMessage(), e);
        }
    }

    /**
     * 持续打印
     * @param call
     */
    @PluginMethod
    public void print(PluginCall call) {
        try {
            var data = call.getString("data");
            var address = call.getString("address");
            var printType = call.getString("printType");
            implementation.print(address,data,printType);
            call.resolve();
        } catch (RuntimeException e) {
            call.reject(e.getMessage(), e);
        }
    }

    @PluginMethod
    public void disconnect(PluginCall call) {
        try {
            implementation.disconnect();
            call.resolve();
        } catch (RuntimeException e) {
            call.reject(e.getMessage(), e);
        }
    }

    @PluginMethod
    public void connectAndPrint(PluginCall call) throws InterruptedException {
        try {
            var data = call.getString("data");
            var address = call.getString("address");
            var printType = call.getString("printType");
            implementation.connect(getContext(), address);
            implementation.print(address, data,printType);
            Thread.sleep(100);
            implementation.disconnect();
            call.resolve();
        } catch (RuntimeException e) {
            call.reject(e.getMessage(), e);
        }
    }
    
}
