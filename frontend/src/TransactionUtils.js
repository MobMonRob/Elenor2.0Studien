class TransactionUtils {
    static formatTimestamp(jsonTimestamp) {
        const date = new Date(
            jsonTimestamp[0], // Jahr
            jsonTimestamp[1] - 1, // Monat (0-basiert, daher -1)
            jsonTimestamp[2], // Tag
            jsonTimestamp[3], // Stunde
            jsonTimestamp[4], // Minuten
            jsonTimestamp[5], // Sekunden
            Math.floor(jsonTimestamp[6] / 1000000) // Millisekunden
        );

        return `${String(date.getDate()).padStart(2, '0')}.${String(date.getMonth() + 1).padStart(2, '0')}.${date.getFullYear()}; ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')} Uhr`;
    }

    static getPaymentTypeTranslation(type) {
        const paymentTypeTranslations = {
            "User2Vcr": "Mitglied zu Kasse",
            "Vcr2User": "Kasse zu Mitglied",
            "Vcr2Vcr": "Kasse zu Kasse",
            "Extern2UserOverVcr": "Extern zu Mitglied über Kasse",
            "User2ExternOverVcr": "Mitglied zu Extern über Kasse",
            "User2User": "Mitglied zu Mitglied",
        };
        return paymentTypeTranslations[type] || null;
    }
}

export default TransactionUtils;