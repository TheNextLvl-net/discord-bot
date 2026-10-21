import type { Command } from "./command.ts";
import { fork } from "./fork.ts";
import { language } from "./language.ts";
import { logs } from "./logs.ts";
import { spark } from "./spark.ts";
import { version } from "./version.ts";

export const commands: ReadonlyMap<string, Command> = new Map(
    [fork, language, logs, spark, version].map(command => [command.data.name, command]),
);
