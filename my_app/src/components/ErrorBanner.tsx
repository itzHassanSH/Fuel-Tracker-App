import "./ErrorBanner.css"


export default function ErrorBanner ({text}:{text: string}) {
    return <div id={"middle_message"} >{text}</div>;
}