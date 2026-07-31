import {useState, useRef} from "react";

export function UseErrorBanner(duration = 3000) {
    const [error, setError] = useState<string | null>(null);
    const timeoutRef = useRef<ReturnType<typeof setTimeout> | undefined>(undefined);

    function showError(err: string) {
        setError(err);
        if (timeoutRef.current) clearTimeout(timeoutRef.current);
        timeoutRef.current = setTimeout(() => setError(null), duration);
    }

    function clearError() {
        if (timeoutRef.current) clearTimeout(timeoutRef.current); // cancel the pending auto-clear, it's redundant now
        setError(null);
    }

    return {error, showError, clearError}
}