import type {
    APIChatInputApplicationCommandInteraction,
    APIInteractionResponse,
    RESTPostAPIChatInputApplicationCommandsJSONBody,
} from "discord-api-types/v10";

export const Color = {
    MOON_YELLOW: 0xF1C40F,
    SEA_GREEN: 0x1F8B4C,
} as const;

export interface Command {
    data: RESTPostAPIChatInputApplicationCommandsJSONBody;

    execute(interaction: APIChatInputApplicationCommandInteraction): APIInteractionResponse | Promise<APIInteractionResponse>;
}
