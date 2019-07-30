package estebes.praescriptum.Utils;

import net.minecraftforge.fluids.FluidStack;

public class FluidUtils {
    /**
     * Simple util to decently represent a FluidStack with a string
     *
     * @param fluidStack the FluidStack to be represent
     * @return a formatted string representing the FluidStack
     */
    public static String toFormattedString(FluidStack fluidStack) {
        return fluidStack.getFluid() == null ? fluidStack.amount + "(mb)x(null)@(unknown)" : fluidStack.toString();
    }
}
