const encoder = new TextEncoder();

function hexToBytes(hex: string): Uint8Array {
    const bytes = new Uint8Array(hex.length / 2);
    for (let i = 0; i < bytes.length; i++) {
        bytes[i] = parseInt(hex.substring(i * 2, i * 2 + 2), 16);
    }
    return bytes;
}

export async function verifyDiscordRequest(request: Request, body: string, publicKey: string): Promise<boolean> {
    const signature = request.headers.get("X-Signature-Ed25519");
    const timestamp = request.headers.get("X-Signature-Timestamp");
    if (!signature || !timestamp) return false;

    try {
        const key = await crypto.subtle.importKey("raw", hexToBytes(publicKey), { name: "Ed25519" }, false, ["verify"]);
        return await crypto.subtle.verify({ name: "Ed25519" }, key, hexToBytes(signature), encoder.encode(timestamp + body));
    } catch {
        return false;
    }
}
