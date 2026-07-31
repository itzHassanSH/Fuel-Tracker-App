import "../components/LoadingSpinner.css"

interface LoadingSpinnerProps {
    colour?: string,
    size?: "small" | "medium" | "large";
}

export default function LoadingSpinner ({colour = "red", size = "medium"} : LoadingSpinnerProps) {
    return <div className={`loading-spinner loading-spinner--${size}`} style={{borderTopColor: colour}}>

    </div>
}